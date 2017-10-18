package com.ipvans.flickrgallery.ui;

import android.app.Application;

import com.ipvans.flickrgallery.di.ApplicationComponent;
import com.ipvans.flickrgallery.di.DaggerApplicationComponent;

public class App extends Application {

    public static ApplicationComponent COMPONENT;

    @Override
    public void onCreate() {
        super.onCreate();

        COMPONENT = DaggerApplicationComponent.builder()
                .application(this)
                .build();
    }
}
