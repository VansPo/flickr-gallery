package com.ipvans.flickrgallery.ui.main;

import com.ipvans.flickrgallery.data.model.Feed;

public class MainViewState {

    private final boolean loading;
    private final Throwable error;
    private final Feed data;
    private final String tags;

    public MainViewState(boolean loading, Throwable error, Feed data, String tags) {
        this.loading = loading;
        this.error = error;
        this.data = data;
        this.tags = tags;
    }

    public boolean isLoading() {
        return loading;
    }

    public Throwable getError() {
        return error;
    }

    public Feed getData() {
        return data;
    }

    public String getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "MainViewState{" +
                "loading=" + loading +
                ", error=" + error +
                ", data=" + data +
                ", tags='" + tags + '\'' +
                '}';
    }
}
