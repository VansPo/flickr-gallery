package com.ipvans.flickrgallery.ui.main;

import android.util.Log;

import com.ipvans.flickrgallery.data.SchedulerProvider;
import com.ipvans.flickrgallery.di.PerActivity;
import com.ipvans.flickrgallery.domain.FeedInteractor;
import com.ipvans.flickrgallery.domain.UpdateEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import static com.ipvans.flickrgallery.ui.main.MainViewState.*;

@PerActivity
public class MainPresenterImpl implements MainPresenter<MainViewState> {

    private final FeedInteractor interactor;
    private final SchedulerProvider schedulers;

    private BehaviorSubject<MainViewState> stateSubject = BehaviorSubject.createDefault(empty());
    private PublishSubject<UpdateEvent> searchSubject = PublishSubject.create();

    private Disposable disposable = new CompositeDisposable();

    @Inject
    public MainPresenterImpl(FeedInteractor interactor, SchedulerProvider schedulers) {
        this.interactor = interactor;
        this.schedulers = schedulers;
    }

    @Override
    public void onAttach() {
        Observable.combineLatest(searchSubject
                        .debounce(150, TimeUnit.MILLISECONDS, schedulers.io())
                        .doOnNext(interactor::getFeed),
                interactor.observe(),
                (tags, feed) -> new MainViewState(feed.isLoading(),
                        feed.getError(), feed.getData(), tags.getTags()))
                .withLatestFrom(stateSubject,
                        (newState, oldState) -> new MainViewState(
                                newState.isLoading(), newState.getError(),
                                newState.getData() != null ? newState.getData() : oldState.getData(),
                                newState.getTags()
                        ))
                .observeOn(schedulers.io())
                .subscribeWith(stateSubject)
                .onSubscribe(disposable);
    }

    @Override
    public void onDetach() {
        disposable.dispose();
    }

    @Override
    public void restoreState(MainViewState data) {
        stateSubject.onNext(data);
    }

    @Override
    public Observable<MainViewState> observe() {
        return stateSubject;
    }

    @Override
    public MainViewState getLatestState() {
        return stateSubject.getValue();
    }

    @Override
    public void search(String tags, boolean force) {
        searchSubject.onNext(new UpdateEvent(tags, force));
    }
}
