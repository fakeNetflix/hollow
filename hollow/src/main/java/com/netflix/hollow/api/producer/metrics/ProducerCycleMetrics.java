package com.netflix.hollow.api.producer.metrics;

import java.util.Optional;
import java.util.OptionalLong;

public class ProducerCycleMetrics {

    private long consecutiveFailures;
    private OptionalLong dataSizeBytesOptional;                     // Announced Data size, only applicable to successful announcements
    private OptionalLong cycleDurationMillisOptional;               // Cycle duration, only applicable to completed cycles
    private OptionalLong announcementDurationMillisOptional;        // Announcement duration, only applicable to completed cycles (skipped cycles dont announce)
    private Optional<Boolean> isCycleSuccessOptional;               // true if cycle was successful, false if cycle failed, not applicable if cycle was skipped
    private Optional<Boolean> isAnnouncementSuccessOptional;        // true if announcement was successful, false if announcement failed, not applicable if cycle was skipped
    private OptionalLong lastCycleSuccessTimeNanoOptional;          // monotonic time of last successful cycle
    private OptionalLong lastAnnouncementSuccessTimeNanoOptional;   // monotonic time of last successful announcement


    public long getConsecutiveFailures() {
        return consecutiveFailures;
    }
    public OptionalLong getDataSizeBytesOptional() {
        return dataSizeBytesOptional;
    }
    public OptionalLong getCycleDurationMillisOptional() {
        return cycleDurationMillisOptional;
    }
    public OptionalLong getAnnouncementDurationMillisOptional() {
        return announcementDurationMillisOptional;
    }
    public Optional<Boolean> getIsCycleSuccessOptional() {
        return isCycleSuccessOptional;
    }
    public Optional<Boolean> getIsAnnouncementSuccessOptional() {
        return isAnnouncementSuccessOptional;
    }
    public OptionalLong getLastCycleSuccessTimeNanoOptional() {
        return lastCycleSuccessTimeNanoOptional;
    }
    public OptionalLong getLastAnnouncementSuccessTimeNanoOptional() {
        return lastAnnouncementSuccessTimeNanoOptional;
    }

    private ProducerCycleMetrics(Builder builder) {
        this.consecutiveFailures = builder.consecutiveFailures;
        this.dataSizeBytesOptional = builder.dataSizeBytesOptional;
        this.cycleDurationMillisOptional = builder.cycleDurationMillisOptional;
        this.announcementDurationMillisOptional = builder.announcementDurationMillisOptional;
        this.isCycleSuccessOptional = builder.isCycleSuccessOptional;
        this.isAnnouncementSuccessOptional = builder.isAnnouncementSuccessOptional;
        this.lastCycleSuccessTimeNanoOptional = builder.lastCycleSuccessTimeNanoOptional;
        this.lastAnnouncementSuccessTimeNanoOptional = builder.lastAnnouncementSuccessTimeNanoOptional;
    }

    public static final class Builder {
        private long consecutiveFailures;
        private OptionalLong dataSizeBytesOptional;
        private OptionalLong cycleDurationMillisOptional;
        private OptionalLong announcementDurationMillisOptional;
        private Optional<Boolean> isCycleSuccessOptional;
        private Optional<Boolean> isAnnouncementSuccessOptional;
        private OptionalLong lastCycleSuccessTimeNanoOptional;
        private OptionalLong lastAnnouncementSuccessTimeNanoOptional;

        public Builder() {
            dataSizeBytesOptional = OptionalLong.empty();
            lastCycleSuccessTimeNanoOptional = OptionalLong.empty();
            lastAnnouncementSuccessTimeNanoOptional = OptionalLong.empty();
        }

        public Builder setConsecutiveFailures(long consecutiveFailures) {
            this.consecutiveFailures = consecutiveFailures;
            return this;
        }
        public Builder setDataSizeBytesOptional(OptionalLong dataSizeBytesOptional) {
            this.dataSizeBytesOptional = dataSizeBytesOptional;
            return this;
        }
        public Builder setCycleDurationMillisOptional(OptionalLong cycleDurationMillisOptional) {
            this.cycleDurationMillisOptional = cycleDurationMillisOptional;
            return this;
        }
        public Builder setAnnouncementDurationMillisOptional(OptionalLong announcementDurationMillisOptional) {
            this.announcementDurationMillisOptional = announcementDurationMillisOptional;
            return this;
        }
        public Builder setIsCycleSuccessOptional(Optional<Boolean> isCycleSuccessOptional) {
            this.isCycleSuccessOptional = isCycleSuccessOptional;
            return this;
        }
        public Builder setIsAnnouncementSuccessOptional(Optional<Boolean> isAnnouncementSuccessOptional) {
            this.isAnnouncementSuccessOptional = isAnnouncementSuccessOptional;
            return this;
        }
        public Builder setLastCycleSuccessTimeNanoOptional(OptionalLong lastCycleSuccessTimeNanoOptional) {
            this.lastCycleSuccessTimeNanoOptional = lastCycleSuccessTimeNanoOptional;
            return this;
        }
        public Builder setLastAnnouncementSuccessTimeNanoOptional(OptionalLong lastAnnouncementSuccessTimeNanoOptional) {
            this.lastAnnouncementSuccessTimeNanoOptional = lastAnnouncementSuccessTimeNanoOptional;
            return this;
        }

        public ProducerCycleMetrics build() {
            return new ProducerCycleMetrics(this);
        }
    }
}
