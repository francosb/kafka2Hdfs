package com.github.kafka2Hdfs.streaming.config;


public interface SparkConstants {

    String MASTER = "yarn";
    String MODE = "append";
    String FORMAT = "parquet";
    String COMPRESSION = "gzip";
    Integer NUM_FILES = 2;
    String EXECUTOR_MEMORY = "5g";
    String EXECUTOR_CORES = "6";
    String EXECUTOR_INSTANCES = "8";
    String DRIVER_MEMORY = "4g";
    String CLEANER_TTL = "300";
    String OFFSET_RESET = "earliest";
    String MAX_RATE_PER_PARTITION = "850"; // NumTopicPartition * 850 = Messages/second
    String SUMARY_METADATA = "false";
    String MERGE_SCHEMA = "false";
    String FILTER_PUSHDOWN = "true";
    String HIVE_SQL_STATEMENT = "MSCK REPAIR TABLE %s";
    String IMPALA_SQL_STATEMENT = "INVALIDATE METADATA %s";
    String CONNECTION_URL = "jdbc:impala://%s:%s;AuthMech=3;UID=%s;PWD=%s;";
    String JDBC_DRIVER_NAME = "com.cloudera.impala.jdbc41.Driver";
    String IMPALA_HOST = "impala.host";
    String IMPALA_PORT = "impala.post";
    String IMPALA_USER = "impala.user";
    String IMPALA_PASS = "impala.pass";


}
