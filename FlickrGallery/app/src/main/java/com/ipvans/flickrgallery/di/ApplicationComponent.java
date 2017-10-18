package com.ipvans.flickrgallery.di;

import android.content.Context;

import com.ipvans.flickrgallery.data.DataModule;
import com.ipvans.flickrgallery.data.SchedulerProvider;
import com.ipvans.flickrgallery.data.model.Feed;
import com.ipvans.flickrgallery.data.repository.FeedRepository;
import com.ipvans.flickrgallery.data.source.storage.Storage;
import com.ipvans.flickrgallery.domain.DomainModule;
import com.ipvans.flickrgallery.domain.FeedInteractor;

import dagger.BindsInstance;
import dagger.Component;

@ApplicationScope
@Component(modules = {DataModule.class, DomainModule.class})
public interface ApplicationComponent {

    SchedulerProvider provideScheduler();

    Storage<Feed> provideStorage();

    FeedRepository provideFeedRepository();

    FeedInteractor provideFeedInteractor();

    Context provideContext();

    @Component.Builder
    interface Builder {

        ApplicationComponent build();

        @BindsInstance
        Builder application(Context context);

    }

}
