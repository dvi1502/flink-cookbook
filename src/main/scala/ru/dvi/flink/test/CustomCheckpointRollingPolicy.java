package ru.dvi.flink.test;

import org.apache.flink.streaming.api.functions.sink.filesystem.PartFileInfo;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.CheckpointRollingPolicy;

class CustomCheckpointRollingPolicy<IN, BucketID> extends CheckpointRollingPolicy<IN, BucketID> {
    private static final long serialVersionUID = 1L;

    CustomCheckpointRollingPolicy() {}

    public boolean shouldRollOnEvent(PartFileInfo<BucketID> partFileState, IN element) {
        return true;
    }

    public boolean shouldRollOnProcessingTime(PartFileInfo<BucketID> partFileState, long currentTime) {
        return false;
    }

}