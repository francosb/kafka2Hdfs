package com.github.kafka2Hdfs;

import com.github.kafka2Hdfs.streaming.config.DBConfiguration;
import com.github.kafka2Hdfs.streaming.config.KafkaConfiguration;
import com.github.kafka2Hdfs.streaming.config.SparkConfiguration;
import com.github.kafka2Hdfs.streaming.spark.StreamProcessing;
import picocli.CommandLine;
import scala.Serializable;

import java.util.Optional;


public class CommandLineRunner implements
        SparkConfiguration,
        KafkaConfiguration,
        DBConfiguration,
        Runnable,
        Serializable {

    public static Optional<CommandLineRunner> initialize(final String[] args) {
        final CommandLineRunner commandLineRunner = new CommandLineRunner();
        new CommandLine(commandLineRunner).parseArgs(args);
        if (commandLineRunner.usageHelpRequested) {
            CommandLine.usage(new CommandLineRunner(), System.out);
            return Optional.empty();
        } else {
            return Optional.of(commandLineRunner);
        }
    }

    // sudo -u hive spark-submit --master yarn --deploy-mode cluster --name straming_datos_hb --jars hdfs://HdfsServiceDevelop:8020/user/prueba/voltdb/ImpalaJDBC41.jar
    // --class com.github.kafka2Hdfs.CommandLineRunner hdfs://HdfsServiceDevelop:8020/user/spark/hotbilling/app/kafka2Hdfs.jar --name app_datos --ihost ddn2.sis.personal.net.py --iport 21050
    // --iuser impala --ipass kGnJ8VMeZHAy --topic datos_hb --brokers ddn1.sis.personal.net.py:9092,ddn2.sis.personal.net.py:9092 --groupid datos_group
    // --bduration 60 --cdir hdfs://HdfsServiceDevelop:8020/user/spark/hotbilling/checkpoint
    // --sstring numero_linea_a,fecha_inicio,fecha_registro,charging_id,rating_group,service_identifier,local_record_sequence_number,record_sequence_number,imsi,imei,
    // ggsn_address,sgsn_address,asigned_address,apn,node_id,charging_characteristics,radio_access_techno_type ,qos_negotiated ,cell_id,network_code,es_roaming,
    // volumen_datos_subida,volumen_datos_bajada,duracion_segundos,result_code,failure_handling_continue,cdr_file,clave_particion
    // --tpath hdfs://HdfsServiceDevelop:8020/user/hive/warehouse/datos_hb --partitions year,month,day

    public static void main(final String[] args) {
        initialize(args).ifPresent(CommandLineRunner::run);
    }

    @CommandLine.Option(
            names = {"--name"},
            description = "Spark app name",
            required = true
    )
    private String appName;

    @CommandLine.Option(
            names = {"--ihost"},
            description = "Impala host",
            required = true
    )
    private String impalaHost;

    @CommandLine.Option(
            names = {"--iport"},
            description = "Impala port",
            required = true
    )
    private String impalaPort;

    @CommandLine.Option(
            names = {"--iuser"},
            description = "Impala username",
            required = true
    )
    private String impalaUser;

    @CommandLine.Option(
            names = {"--ipass"},
            description = "Impala password",
            required = true
    )
    private String impalaPass;

    @CommandLine.Option(
            names = {"--topic"},
            description = "Kafka topic name",
            required = true
    )
    private String topic;

    @CommandLine.Option(
            names = {"--brokers"},
            description = "Kafka brokers",
            required = true
    )
    private String brokers;

    @CommandLine.Option(
            names = {"--groupid"},
            description = "Kafka consumers group id",
            required = true
    )
    private String groupId;

    @CommandLine.Option(
            names = {"--bduration"},
            description = "Spark batch duration",
            required = true
    )
    private String batchDuration;

    @CommandLine.Option(
            names = {"--cdir"},
            description = "Spark app checkpoint dir",
            required = true
    )
    private String checkpointDir;

    @CommandLine.Option(
            names = {"--yqueue"},
            description = "YARN queue name"
    )
    private String yarnQueue;

    @CommandLine.Option(
            names = {"--sstring"},
            description = "DF schema string",
            required = true
    )
    private String schemaString;

    @CommandLine.Option(
            names = {"--tpath"},
            description = "Destination table path",
            required = true
    )
    private String tablePath;

    @CommandLine.Option(
            names = {"--partitions"},
            description = "Hive table patitions",
            required = true
    )
    private String partitions;

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true,
            description = "Display this message")
    private boolean usageHelpRequested;

    @Override
    public String getImpalaHost() {
        return impalaHost;
    }

    @Override
    public String getImpalaPort() {
        return impalaPort;
    }

    @Override
    public String getImpalaUser() {
        return impalaUser;
    }

    @Override
    public String getImpalaPass() {
        return impalaPass;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public String getBrokers() {
        return brokers;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public String getBatchDuration() {
        return batchDuration;
    }

    @Override
    public String getCheckpointDir() {
        return checkpointDir;
    }

    @Override
    public String getAppName() {
        return appName;
    }

    @Override
    public String getYarnQueue() {
        return yarnQueue;
    }

    @Override
    public String getSchemaString() {
        return schemaString;
    }

    @Override
    public String getTablePath() {
        return tablePath;
    }

    @Override
    public String getPartitions() {
        return partitions;
    }

@Override
public void run() {
    try {
        new StreamProcessing(
                this,
                this,
                this
        ).startStreaming();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
