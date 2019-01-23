package com.netflix.hollow.api.producer.metrics;

import com.netflix.hollow.api.producer.HollowProducer;
import com.netflix.hollow.api.producer.Status;
import com.netflix.hollow.api.producer.listener.AnnouncementListener;
import com.netflix.hollow.api.producer.listener.CycleListener;
import java.time.Duration;

/**
 * This class provides a base class for overriding all the stages of a Hollow producer cycle, so that individual listeners
 * can override the limited set of stages that they care about. For eg. the metrics listener at {@code AbstractCycleMetricsListener}
 * cares about a subset of these stages for computing and reporting producer metrics so it extends this abstract class, and
 * implements the {@code CycleMetricsReporting} interface which makes it mandatory for extending subclasses to provide a
 * metrics reporting implementation.
 */
public abstract class AbstractCycleListener implements CycleListener, AnnouncementListener {

    @Override
    public void onCycleSkip(CycleSkipReason reason) {
        // no-op
    }

    @Override
    public void onNewDeltaChain(long version) {
        // no-op
    }

    @Override
    public void onCycleStart(long version) {
        // no-op
    }

    @Override
    public void onCycleComplete(Status status, HollowProducer.ReadState readState, long version, Duration elapsed) {
        // no-op
    }

    @Override
    public void onAnnouncementStart(long version) {
        // no-op
    }

    @Override
    public void onAnnouncementComplete(Status status, HollowProducer.ReadState readState, long version, Duration elapsed) {
        // no-op
    }
}
