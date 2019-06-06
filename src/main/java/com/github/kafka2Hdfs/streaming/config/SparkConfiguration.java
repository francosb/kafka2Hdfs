package com.github.kafka2Hdfs.streaming.config;

import org.apache.spark.SparkConf;

public interface SparkConfiguration {

    String getBatchDuration();

    String getCheckpointDir();

    String getAppName();

    String getYarnQueue();

    String getSchemaString();

    String getTablePath();

    String getPartitions();

    default String getMaster() {
        return SparkConstants.MASTER;
    }

    default String getExecutorMemory() {
        return SparkConstants.EXECUTOR_MEMORY;
    }

    default String getExecutorCores() {
        return SparkConstants.EXECUTOR_CORES;
    }

    default String getMaxRatePerPartition() {
        return SparkConstants.MAX_RATE_PER_PARTITION;
    }

    default String getExecutorInstances() {
        return SparkConstants.EXECUTOR_INSTANCES;
    }

    default String getDriverMemory() {
        return SparkConstants.DRIVER_MEMORY;
    }

    default String getSumaryMetadata() {
        return SparkConstants.SUMARY_METADATA;
    }

    default String getMergeSchema() {
        return SparkConstants.MERGE_SCHEMA;
    }

    default String getFilterPushdown() {
        return SparkConstants.FILTER_PUSHDOWN;
    }

    default String getCleanerTtl() {
        return SparkConstants.CLEANER_TTL;
    }

    default SparkConf getSparkConf() {
        SparkConf sparkConf = new SparkConf();
        sparkConf.set("spark.executor.memory", getExecutorMemory());
        sparkConf.set("spark.executor.cores", getExecutorCores());
        sparkConf.set("spark.yarn.queue", getYarnQueue());
        sparkConf.set("spark.streaming.kafka.maxRatePerPartition", getMaxRatePerPartition());
        sparkConf.set("spark.executor.instances", getExecutorInstances());
        sparkConf.set("spark.driver.memory", getDriverMemory());
        sparkConf.set("spark.hadoop.parquet.enable.summary-metadata", getSumaryMetadata());
        sparkConf.set("spark.sql.parquet.mergeSchema", getMergeSchema());
        sparkConf.set("spark.sql.parquet.filterPushdown", getFilterPushdown());
        sparkConf.set("spark.cleaner.ttl", getCleanerTtl());

        return sparkConf;
    }

}
