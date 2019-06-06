package com.github.kafka2Hdfs.streaming.config;

import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashMap;
import java.util.Map;


public interface KafkaConfiguration {

    String getTopic();

    String getBrokers();

    String getGroupId();

    default String getOffsetReset() {
        return KafkaConstants.OFFSET_RESET;
    }

    default String getAutocommit() {
        return KafkaConstants.ENABLE_AUTOCOMMIT;
    }

    default Map<String, Object> getKafkaConfig() {
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", getBrokers());
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", getGroupId());
        kafkaParams.put("auto.offset.reset", getOffsetReset());
        kafkaParams.put("enable.auto.commit", getAutocommit());

        return kafkaParams;
    }




}
