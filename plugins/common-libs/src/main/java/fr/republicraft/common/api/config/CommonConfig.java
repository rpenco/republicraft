package fr.republicraft.common.api.config;

import lombok.Data;

@Data
public class CommonConfig {
    /**
     * Database configuration
     */
    ConnectionConfig connection;

    /**
     * Metric reporter config
     */
    MetricReportersConfig metrics;

}
