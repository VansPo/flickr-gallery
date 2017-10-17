package com.ipvans.flickrgallery.domain;

import android.text.TextUtils;

import com.ipvans.flickrgallery.data.model.Feed;
import com.ipvans.flickrgallery.data.repository.FeedRepository;
import com.ipvans.flickrgallery.di.ApplicationScope;
import com.ipvans.flickrgallery.domain.model.Response;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import static com.ipvans.flickrgallery.domain.model.Response.loading;

@ApplicationScope
public class FeedInteractorImpl implements FeedInteractor {

    private final FeedRepository feedRepository;

    private BehaviorSubject<Response<Feed>> responseSubject = BehaviorSubject.createDefault(loading());
    private PublishSubject<List<String>> updateSubject = PublishSubject.create();

    @Inject
    public FeedInteractorImpl(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;

        updateSubject.map(tags -> TextUtils.join(",", tags))
                .doOnNext(ignored -> responseSubject.onNext(loading()))
                .switchMap(it ->
                        feedRepository.getFeed(it, false).toObservable()
                                .map(Response::ok)
                                .onErrorReturn(Response::error)
                )
                .subscribe(responseSubject);
    }


    @Override
    public Observable<Response<Feed>> observe() {
        return responseSubject;
    }

    @Override
    public void getFeed(List<String> tags, boolean forceUpdate) {

    }
}
