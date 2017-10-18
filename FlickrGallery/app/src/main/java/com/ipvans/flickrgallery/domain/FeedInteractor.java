package com.ipvans.flickrgallery.domain;

import com.ipvans.flickrgallery.data.model.Feed;
import com.ipvans.flickrgallery.domain.model.Response;

import io.reactivex.Observable;

public interface FeedInteractor {

    Observable<Response<Feed>> observe();

    void getFeed(UpdateEvent event);

}
