
package com.example.marek.dto;

public class PlaybackTime {

    private final long totalSeconds;
    private final boolean estimated;
    private final int tracksWithoutDuration;

    public PlaybackTime(
            long totalSeconds,
            boolean estimated,
            int tracksWithoutDuration
    ) {
        this.totalSeconds = totalSeconds;
        this.estimated = estimated;
        this.tracksWithoutDuration = tracksWithoutDuration;
    }

    public long getTotalSeconds() {
        return totalSeconds;
    }

    public boolean isEstimated() {
        return estimated;
    }

    public int getTracksWithoutDuration() {
        return tracksWithoutDuration;
    }
}
