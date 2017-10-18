package com.ipvans.flickrgallery.mock;

import com.hadisatrio.optional.Optional;
import com.ipvans.flickrgallery.data.model.Feed;
import com.ipvans.flickrgallery.data.source.storage.Storage;

import io.reactivex.Single;

public class MemoryStorage implements Storage<Feed> {

    private Feed item;

    @Override
    public Single<Optional<Feed>> get() {
        return Single.just(Optional.ofNullable(item));
    }

    @Override
    public void save(Feed item) {
        this.item = item;
    }
}
