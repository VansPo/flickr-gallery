package com.ipvans.flickrgallery.data.source.api;

import com.ipvans.flickrgallery.data.model.Feed;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrRestService {

    @GET("/services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    Single<Feed> getFeed(@Query("format") String format,
                         @Query("tags") String tags,
                         @Query("nojsoncallback") int noJson);

}
