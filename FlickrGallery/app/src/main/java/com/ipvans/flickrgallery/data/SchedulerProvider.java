package com.ipvans.flickrgallery.data;

import io.reactivex.Scheduler;

public interface SchedulerProvider {

    Scheduler io();

    Scheduler mainThread();

}
