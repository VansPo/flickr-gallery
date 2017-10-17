package com.ipvans.flickrgallery.data.repository;

import com.ipvans.flickrgallery.data.model.Feed;

import io.reactivex.Single;

public interface FeedRepository {

    Single<Feed> getFeed(String tags, boolean forceUpdate);

}
