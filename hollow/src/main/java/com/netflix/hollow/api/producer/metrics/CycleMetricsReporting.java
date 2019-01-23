package com.netflix.hollow.api.producer.metrics;

/**
 * Allows implementations to plug in reporting of metrics related to a producer cycle
 */
public interface CycleMetricsReporting {

    void cycleEndMetricsReporting(ProducerCycleMetrics producerCycleMetrics);
}
