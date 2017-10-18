package com.ipvans.flickrgallery.data.repository;

import com.hadisatrio.optional.Optional;
import com.ipvans.flickrgallery.data.model.Feed;
import com.ipvans.flickrgallery.data.source.api.FlickrRestService;
import com.ipvans.flickrgallery.data.source.storage.Storage;
import com.ipvans.flickrgallery.di.ApplicationScope;

import javax.inject.Inject;

import io.reactivex.Single;

@ApplicationScope
public class FeedRepositoryImpl implements FeedRepository {

    private static final String FORMAT = "json";
    private static final int NO_CALLBACK = 1;

    private final FlickrRestService api;
    private final Storage<Feed> storage;

    @Inject
    public FeedRepositoryImpl(FlickrRestService api, Storage<Feed> storage) {
        this.api = api;
        this.storage = storage;
    }

    @Override
    public Single<Feed> getFeed(String tags, boolean forceUpdate) {
        return Single.just(forceUpdate)
                .flatMap(force -> {
                            if (force) {
                                return getFromApi(tags);
                            } else {
                                return Single.just(storage.get())
                                        .flatMap(it -> it.blockingGet().isPresent() ?
                                                it.map(Optional::get) : getFromApi(tags));
                            }
                        }
                );
    }

    private Single<Feed> getFromApi(String tags) {
        return api.getFeed(FORMAT, tags, NO_CALLBACK).doOnSuccess(storage::save);
    }

}
