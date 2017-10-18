package com.ipvans.flickrgallery.ui.main;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class MainViewModule {

    @Binds
    abstract MainPresenter<MainViewState> providePresenter(MainPresenterImpl mainPresenter);

}
