package com.github.kafka2Hdfs.streaming.config;


public interface KafkaConstants {

    Integer TIME_FOR_TRIGGER = 1;
    String OFFSET_RESET = "earliest";
    String ENABLE_AUTOCOMMIT = "false"; // NumTopicPartition * 850 = Messages/second

}
