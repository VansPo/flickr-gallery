package com.ipvans.flickrgallery.domain;

import com.ipvans.flickrgallery.data.model.Feed;
import com.ipvans.flickrgallery.domain.model.Response;

import java.util.List;

import io.reactivex.Observable;

public interface FeedInteractor {

    Observable<Response<Feed>> observe();

    void getFeed(List<String> tags, boolean forceUpdate);

}
