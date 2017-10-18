package com.ipvans.flickrgallery.mock;

import com.ipvans.flickrgallery.data.SchedulerProvider;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class TestSchedulerProvider implements SchedulerProvider {
    @Override
    public Scheduler io() {
        return Schedulers.trampoline();
    }

    @Override
    public Scheduler mainThread() {
        return Schedulers.trampoline();
    }
}
