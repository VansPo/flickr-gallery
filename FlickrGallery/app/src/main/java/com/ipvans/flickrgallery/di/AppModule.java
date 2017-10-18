package com.ipvans.flickrgallery.di;

import android.content.Context;

import dagger.Module;

@ApplicationScope
@Module
public class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }


}
