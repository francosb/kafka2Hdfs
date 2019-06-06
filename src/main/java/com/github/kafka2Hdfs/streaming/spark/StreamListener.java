package com.github.kafka2Hdfs.streaming.spark;

import com.github.kafka2Hdfs.streaming.config.DBConfiguration;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.scheduler.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.kafka2Hdfs.streaming.config.KafkaConfiguration;


public class StreamListener implements StreamingListener {
    private KafkaConfiguration kafkaConfiguration;
    private DBConfiguration DBConfiguration;
    private SparkSession sparkSession;


    public StreamListener(KafkaConfiguration kafkaConfiguration,
                          SparkSession sparkSession,
                          DBConfiguration DBConfiguration) {
        this.kafkaConfiguration = kafkaConfiguration;
        this.DBConfiguration = DBConfiguration;
        this.sparkSession = sparkSession;
    }


    @Override
    public void onStreamingStarted(StreamingListenerStreamingStarted streamingListenerStreamingStarted) {

    }

    @Override
    public void onReceiverStarted(StreamingListenerReceiverStarted streamingListenerReceiverStarted) {

    }

    @Override
    public void onReceiverError(StreamingListenerReceiverError streamingListenerReceiverError) {

    }

    @Override
    public void onReceiverStopped(StreamingListenerReceiverStopped streamingListenerReceiverStopped) {

    }

    @Override
    public void onBatchSubmitted(StreamingListenerBatchSubmitted streamingListenerBatchSubmitted) {

    }

    @Override
    public void onBatchStarted(StreamingListenerBatchStarted streamingListenerBatchStarted) {

    }

    /**
     * Called when processing of a batch of jobs has completed.
     *
     * @param streamingListenerBatchCompleted
     */
    @Override
    public void onBatchCompleted(StreamingListenerBatchCompleted streamingListenerBatchCompleted) {
        hiveMetadataUpdate(sparkSession, kafkaConfiguration);
    }

    @Override
    public void onOutputOperationStarted(StreamingListenerOutputOperationStarted streamingListenerOutputOperationStarted) {

    }

    @Override
    public void onOutputOperationCompleted(StreamingListenerOutputOperationCompleted streamingListenerOutputOperationCompleted) {

    }

    /**
     * Execute msck repair table to add metadata about the partitions to the Hive catalogs.
     * @param sparkSession
     * @param kafkaConfiguration
     */
    private void hiveMetadataUpdate(SparkSession sparkSession,
                                    KafkaConfiguration kafkaConfiguration) {
        sparkSession.sql(String.format(DBConfiguration.getHiveSqlStatement(), kafkaConfiguration.getTopic()));
    }
}
