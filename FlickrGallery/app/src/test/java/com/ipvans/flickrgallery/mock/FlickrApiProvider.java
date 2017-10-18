package com.ipvans.flickrgallery.mock;

import com.ipvans.flickrgallery.data.source.api.FlickrRestService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public class FlickrApiProvider {

    public static FlickrRestService provideRestService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(System.out::println);
        logging.setLevel(BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        return new Retrofit.Builder()
                .baseUrl("https://api.flickr.com/")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FlickrRestService.class);
    }

}
