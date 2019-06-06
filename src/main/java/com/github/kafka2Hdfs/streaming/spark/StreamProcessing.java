package com.github.kafka2Hdfs.streaming.spark;

import com.github.kafka2Hdfs.streaming.config.DBConfiguration;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function0;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.kafka2Hdfs.streaming.config.KafkaConfiguration;
import com.github.kafka2Hdfs.streaming.config.SparkConfiguration;
import com.github.kafka2Hdfs.streaming.dto.SlotMessageDTO;
import scala.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.github.kafka2Hdfs.streaming.config.SparkConstants.NUM_FILES;
import static com.github.kafka2Hdfs.streaming.config.SparkConstants.COMPRESSION;
import static com.github.kafka2Hdfs.streaming.config.SparkConstants.MODE;
import static com.github.kafka2Hdfs.streaming.config.SparkConstants.FORMAT;


public class StreamProcessing implements Serializable {
    private final SparkConfiguration sparkConfiguration;
    private final KafkaConfiguration kafkaConfiguration;
    private final DBConfiguration dbConfiguration;
    private SparkSession sparkSession;

    public StreamProcessing(SparkConfiguration sparkConfiguration,
                            KafkaConfiguration kafkaConfiguration,
                            DBConfiguration dbConfiguration) {
        this.sparkConfiguration = sparkConfiguration;
        this.kafkaConfiguration = kafkaConfiguration;
        this.dbConfiguration = dbConfiguration;
    }

    JavaStreamingContext getStreamingContext() {
        sparkSession = getSparkSession(sparkConfiguration);
        JavaStreamingContext javaStreamingContext = getJavaStreamingContext(sparkSession);
        JavaInputDStream<ConsumerRecord<String, String>> javaInputDStream = getDStream(kafkaConfiguration,
                javaStreamingContext);

        JavaPairDStream<String, String> msgStream = javaInputDStream.mapToPair(record ->
                new Tuple2<>(Long.toString(record.offset()), record.value()));

        JavaDStream<Row> processedStream = msgStream
                .map(x -> String.join(",", x._2().split("\n")))
                .map(value -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    SlotMessageDTO slotMessageDTO = objectMapper.readValue(value, SlotMessageDTO.class);

                    slotMessageDTO.getChange().get(0).getColumnvalues()
                            .add(String.valueOf(slotMessageDTO.getXid()));
                    slotMessageDTO.getChange().get(0).getColumnvalues()
                            .add(String.valueOf(slotMessageDTO.getNextlsn()));
                    slotMessageDTO.getChange().get(0).getColumnvalues()
                            .add(String.valueOf(slotMessageDTO.getTimestamp()));
                    slotMessageDTO.getChange().get(0).getColumnvalues()
                            .add(String.valueOf(slotMessageDTO.getChange().get(0).getYear()));
                    slotMessageDTO.getChange().get(0).getColumnvalues()
                            .add(String.valueOf(slotMessageDTO.getChange().get(0).getMonth()));
                    slotMessageDTO.getChange().get(0).getColumnvalues()
                            .add(String.valueOf(slotMessageDTO.getChange().get(0).getDay()));

                    return RowFactory.create(slotMessageDTO.getChange().get(0).getColumnvalues().toArray());
                });

        processedStream.foreachRDD(rdd -> {
            if (!rdd.isEmpty()) {
                Dataset<Row> rowDF = sparkSession.createDataFrame(rdd, getStruct());
                rowDF = rowDF.repartition(NUM_FILES);
                rowDF.write()
                        .option("compression", COMPRESSION)
                        .mode(MODE)
                        .format(FORMAT)
                        .partitionBy(sparkConfiguration.getPartitions().split(","))
                        .save(sparkConfiguration.getTablePath());
                rowDF.unpersist();
            }
        });

        return javaStreamingContext;
    }

    private SparkSession getSparkSession(SparkConfiguration sparkConfiguration) {
        return SparkSession
                .builder()
                .appName(sparkConfiguration.getAppName())
                .master(sparkConfiguration.getMaster())
                .config(sparkConfiguration.getSparkConf())
                .getOrCreate();
    }

    private JavaStreamingContext getJavaStreamingContext(SparkSession sparkSession) {
        JavaStreamingContext jssc = new JavaStreamingContext(new JavaSparkContext(sparkSession.sparkContext()),
                Durations.minutes(Long.parseLong(sparkConfiguration.getBatchDuration())));

        jssc.checkpoint(sparkConfiguration.getCheckpointDir());

        return jssc;
    }

    private JavaInputDStream<ConsumerRecord<String, String>> getDStream(KafkaConfiguration kafkaConfiguration,
                                                                        JavaStreamingContext javaStreamingContext) {

        Collection<String> topics = Collections.singletonList(kafkaConfiguration.getTopic());

        return KafkaUtils.createDirectStream(
                javaStreamingContext,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topics, kafkaConfiguration.getKafkaConfig()));
    }

    private StructType getStruct() {
        List<StructField> fields = new ArrayList<>();
        for (String fieldName : sparkConfiguration.getSchemaString().split(",")) {
            StructField field = DataTypes.createStructField(fieldName, DataTypes.StringType, true);
            fields.add(field);
        }
        return DataTypes.createStructType(fields);
    }

    public void startStreaming() throws InterruptedException {
        Function0<JavaStreamingContext> createContextFunc = (Function0<JavaStreamingContext>) this::getStreamingContext;
        JavaStreamingContext ssc = JavaStreamingContext
                .getOrCreate(sparkConfiguration.getCheckpointDir(), createContextFunc);
        ssc.addStreamingListener(new StreamListener(kafkaConfiguration, sparkSession, dbConfiguration));
        ssc.start();
        ssc.awaitTermination();
    }

}
