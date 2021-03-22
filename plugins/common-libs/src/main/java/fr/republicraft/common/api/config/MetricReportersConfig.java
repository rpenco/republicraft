package fr.republicraft.common.api.config;

import lombok.Data;

@Data
public class MetricReportersConfig {
    /**
     * Socket host
     */
    String host;

    /**
     * Socket port
     */
    int port;

    /**
     * Enabled reporter
     */
    boolean enabled = true;
}
