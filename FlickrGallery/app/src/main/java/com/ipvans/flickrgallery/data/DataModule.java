package com.ipvans.flickrgallery.data;

import com.google.gson.Gson;
import com.ipvans.flickrgallery.data.source.api.FlickrRestService;
import com.ipvans.flickrgallery.di.Endpoint;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DataModule {

    @Provides
    @Endpoint
    String provideEndpoint() {
        return "https://api.flickr.com/";
    }

    @Provides
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    Converter.Factory provideFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    Retrofit provideRetrofitAdapter(Converter.Factory factory, @Endpoint String endpoint) {
        return new Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(factory)
                .build();
    }

    @Provides
    FlickrRestService provideService(Retrofit retrofit) {
        return retrofit.create(FlickrRestService.class);
    }

    @Provides
    SchedulerProvider provideScheduler() {
        return new SchedulerProvider() {
            @Override
            public Scheduler io() {
                return Schedulers.io();
            }

            @Override
            public Scheduler mainThread() {
                return AndroidSchedulers.mainThread();
            }
        };
    }
}
