package com.ipvans.flickrgallery.data.source.storage;

import com.hadisatrio.optional.Optional;

import io.reactivex.Single;

public interface Storage<T> {

    Single<Optional<T>> get();

    void save(T item);

}
