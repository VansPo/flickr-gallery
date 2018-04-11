package com.ipvans.flickrgallery.domain;

import com.ipvans.flickrgallery.data.SchedulerProvider;
import com.ipvans.flickrgallery.data.model.Feed;
import com.ipvans.flickrgallery.data.repository.FeedRepository;
import com.ipvans.flickrgallery.di.ApplicationScope;
import com.ipvans.flickrgallery.domain.model.Response;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import static com.ipvans.flickrgallery.domain.model.Response.loading;

@ApplicationScope
public class FeedInteractorImpl implements FeedInteractor {

    private BehaviorSubject<Response<Feed>> responseSubject = BehaviorSubject.createDefault(loading());
    private PublishSubject<UpdateEvent> updateSubject = PublishSubject.create();

    @Inject
    public FeedInteractorImpl(FeedRepository feedRepository, SchedulerProvider schedulerProvider) {
        updateSubject
                .doOnNext(ignored -> responseSubject.onNext(loading()))
                .switchMap(it ->
                        feedRepository.getFeed(it.getTags(), it.isForced()).toObservable()
                                .subscribeOn(schedulerProvider.io())
                                .map(Response::ok)
                                .onErrorReturn(Response::error)
                )
                .subscribeOn(schedulerProvider.io())
                .subscribe(responseSubject);
    }

    @Override
    public Observable<Response<Feed>> observe() {
        return responseSubject;
    }

    @Override
    public void getFeed(UpdateEvent event) {
        updateSubject.onNext(event);
    }

}
