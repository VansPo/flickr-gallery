package com.ipvans.flickrgallery.domain;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class DomainModule {

    @Binds
    abstract FeedInteractor provideInteractor(FeedInteractorImpl feedInteractor);

}
