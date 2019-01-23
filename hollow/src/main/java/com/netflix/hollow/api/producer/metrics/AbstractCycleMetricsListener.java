package com.netflix.hollow.api.producer.metrics;

import com.netflix.hollow.api.producer.HollowProducer;
import com.netflix.hollow.api.producer.Status;
import com.netflix.hollow.core.read.engine.HollowReadStateEngine;
import com.netflix.hollow.core.read.engine.HollowTypeReadState;
import java.time.Duration;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractCycleMetricsListener extends AbstractCycleListener implements CycleMetricsReporting {

    private static final Logger log = Logger.getLogger(AbstractCycleMetricsListener.class.getName());

    private long consecutiveFailures;
    private OptionalLong lastCycleSuccessTimeNanoOptional;
    private OptionalLong lastAnnouncementSuccessTimeNanoOptional;
    private ProducerCycleMetrics.Builder producerCycleMetricsBuilder;

    public AbstractCycleMetricsListener() {
        consecutiveFailures = 0l;
        lastCycleSuccessTimeNanoOptional = OptionalLong.empty();
        lastAnnouncementSuccessTimeNanoOptional = OptionalLong.empty();
    }

    @Override
    public void onCycleStart(long version) {
        producerCycleMetricsBuilder = new ProducerCycleMetrics.Builder();
    }

    /**
     * Reports metrics for when cycle is skipped due to reasons such as the producer not being the leader in a multiple-producer setting.
     * In a multiple producer setting, leader election typically favors long-lived leaders to avoid producer runs from frequently requiring
     * to reload the full state before publishing data. When a cycle is skipped because the producer wasn't primary, the skipped run is
     * considered as a successful run that published no data.
     * @param reason Reason why the run was skipped
     */
    @Override
    public void onCycleSkip(CycleSkipReason reason) {
        OptionalLong dataSizeOptional = OptionalLong.empty();

        if (reason == CycleSkipReason.NOT_PRIMARY_PRODUCER) {
            // leader election for multiple producers is long-lived, treat cycle skip as success and zero out failures
            consecutiveFailures = 0l;
            // leader node is reporting the data size, so report 0 for current node
            dataSizeOptional = OptionalLong.of(0l);
        }

        producerCycleMetricsBuilder
                .setConsecutiveFailures(consecutiveFailures)
                .setDataSizeBytesOptional(dataSizeOptional)
                .setIsCycleSuccessOptional(Optional.empty())
                .setIsAnnouncementSuccessOptional(Optional.empty())
                .setCycleDurationMillisOptional(OptionalLong.empty())
                .setAnnouncementDurationMillisOptional(OptionalLong.empty())
                .setLastCycleSuccessTimeNanoOptional(lastCycleSuccessTimeNanoOptional)
                .setLastAnnouncementSuccessTimeNanoOptional(lastAnnouncementSuccessTimeNanoOptional);

        cycleEndMetricsReporting(producerCycleMetricsBuilder.build());
    }

    /**
     * Sets announcement metrics on producerCycleMetricsBuilder object, but for simplicity metrics are not reported until
     * the producer cycle is complete
     * @param status Indicates whether the announcement succeeded of failed
     * @param readState Hollow data state that is being published, used in this method for computing data size
     * @param version Version of data that was announced
     * @param elapsed Announcement start to end duration
     */
    @Override
    public void onAnnouncementComplete(Status status, HollowProducer.ReadState readState, long version, Duration elapsed) {
        boolean isAnnouncementSuccess = false;
        OptionalLong dataSizeOptional = OptionalLong.empty();

        if (status.getType() == Status.StatusType.SUCCESS) {
            isAnnouncementSuccess = true;
            lastAnnouncementSuccessTimeNanoOptional = OptionalLong.of(System.nanoTime());
            HollowReadStateEngine stateEngine = readState.getStateEngine();
            dataSizeOptional = OptionalLong.of(
                    stateEngine.getAllTypes()
                            .stream()
                            .map(stateEngine::getTypeState)
                            .mapToLong(HollowTypeReadState::getApproximateHeapFootprintInBytes)
                            .sum());
        }

        producerCycleMetricsBuilder
                .setDataSizeBytesOptional(dataSizeOptional)
                .setIsAnnouncementSuccessOptional(Optional.of(isAnnouncementSuccess))
                .setAnnouncementDurationMillisOptional(OptionalLong.of(elapsed.toMillis()))
                .setLastAnnouncementSuccessTimeNanoOptional(lastAnnouncementSuccessTimeNanoOptional);
    }

    /**
     * On cycle completion this method reports cycle metrics (including announcement metrics).
     * @param status Whether the cycle succeeded or failed
     * @param readState Hollow data state published by cycle, not used here because data size is known from when announcement completed
     * @param version Version of data that was published in this cycle
     * @param elapsed Cycle start to end duration
     */
    @Override
    public void onCycleComplete(Status status, HollowProducer.ReadState readState, long version, Duration elapsed) {
        long cycleEndTimeNano = System.nanoTime();
        Optional<Boolean> isCycleSuccess;

        if (status.getType() == Status.StatusType.SUCCESS) {
            isCycleSuccess = Optional.of(true);
            consecutiveFailures = 0l;
            lastCycleSuccessTimeNanoOptional = OptionalLong.of(cycleEndTimeNano);
        } else {
            isCycleSuccess = Optional.of(false);
            consecutiveFailures ++;
        }

        producerCycleMetricsBuilder
                .setConsecutiveFailures(consecutiveFailures)
                .setCycleDurationMillisOptional(OptionalLong.of(elapsed.toMillis()))
                .setIsCycleSuccessOptional(isCycleSuccess)
                .setLastCycleSuccessTimeNanoOptional(lastCycleSuccessTimeNanoOptional);

        cycleEndMetricsReporting(producerCycleMetricsBuilder.build());
    }
}
