package com.ipvans.flickrgallery.di;

import com.ipvans.flickrgallery.data.DataModule;

import dagger.Component;

@ApplicationScope
@Component(modules = DataModule.class)
public interface ApplicationComponent {
}
