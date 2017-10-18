package com.ipvans.flickrgallery.domain;

public class UpdateEvent {
    private final String tags;
    private final boolean force;

    public UpdateEvent(String tags, boolean force) {
        this.tags = tags;
        this.force = force;
    }

    public String getTags() {
        return tags;
    }

    public boolean isForced() {
        return force;
    }
}