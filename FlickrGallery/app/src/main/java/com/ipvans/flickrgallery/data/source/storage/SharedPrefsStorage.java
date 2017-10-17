package com.ipvans.flickrgallery.data.source.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.hadisatrio.optional.Optional;
import com.ipvans.flickrgallery.data.SchedulerProvider;
import com.ipvans.flickrgallery.data.model.Feed;
import com.ipvans.flickrgallery.di.ApplicationScope;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;


@ApplicationScope
public class SharedPrefsStorage implements Storage<Feed> {

    private static final String NAME = "FlickrStorage";
    private static final String KEY = "Feed";

    private final SharedPreferences preferences;
    private final SchedulerProvider scheduler;
    private final Gson gson;

    @Inject
    public SharedPrefsStorage(Context context, Gson gson, SchedulerProvider schedulerProvider) {
        preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        this.gson = gson;
        scheduler = schedulerProvider;
    }

    @Override
    public Single<Optional<Feed>> get() {
        return Single.fromCallable(() -> {
            String item = preferences.getString(KEY, "");
            if (!item.isEmpty()) {
                return Optional.of(gson.fromJson(item, Feed.class));
            }
            return Optional.absent();
        });
    }

    @Override
    public void save(Feed item) {
        Completable.fromAction(() -> preferences.edit().putString(KEY, gson.toJson(item)).apply())
                .observeOn(scheduler.io())
                .subscribe();
    }
}
