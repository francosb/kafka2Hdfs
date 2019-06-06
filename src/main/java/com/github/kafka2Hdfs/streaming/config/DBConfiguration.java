package com.github.kafka2Hdfs.streaming.config;

import java.util.Properties;

import static com.github.kafka2Hdfs.streaming.config.SparkConstants.*;

public interface DBConfiguration {

    String getImpalaHost();

    String getImpalaPort();

    String getImpalaUser();

    String getImpalaPass();

    default Properties getImpalaConf() {
        Properties properties = new Properties();
        properties.put(IMPALA_HOST, getImpalaHost());
        properties.put(IMPALA_PORT, getImpalaPort());
        properties.put(IMPALA_USER, getImpalaUser());
        properties.put(IMPALA_PASS, getImpalaPass());
        return properties;
    }

    default String getImpalaSqlStatement() {
        return SparkConstants.IMPALA_SQL_STATEMENT;
    }

    default String getImpalaConnectionUrl() {
        return SparkConstants.CONNECTION_URL;
    }

    default String getImpalaJdbcDriverName() {
        return SparkConstants.JDBC_DRIVER_NAME;
    }

    default String getHiveSqlStatement() {
        return SparkConstants.HIVE_SQL_STATEMENT;
    }

}
