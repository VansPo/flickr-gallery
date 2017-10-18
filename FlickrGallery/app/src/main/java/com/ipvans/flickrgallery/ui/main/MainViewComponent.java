package com.ipvans.flickrgallery.ui.main;

import com.ipvans.flickrgallery.di.ApplicationComponent;
import com.ipvans.flickrgallery.di.PerActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = MainViewModule.class)
public interface MainViewComponent {

    void inject(MainActivity view);

}
