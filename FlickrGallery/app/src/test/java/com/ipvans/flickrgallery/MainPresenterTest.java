package com.ipvans.flickrgallery;

import com.ipvans.flickrgallery.data.SchedulerProvider;
import com.ipvans.flickrgallery.data.model.Feed;
import com.ipvans.flickrgallery.data.repository.FeedRepositoryImpl;
import com.ipvans.flickrgallery.data.source.storage.Storage;
import com.ipvans.flickrgallery.domain.FeedInteractorImpl;
import com.ipvans.flickrgallery.mock.FlickrApiProvider;
import com.ipvans.flickrgallery.mock.MemoryStorage;
import com.ipvans.flickrgallery.mock.TestSchedulerProvider;
import com.ipvans.flickrgallery.ui.main.MainPresenter;
import com.ipvans.flickrgallery.ui.main.MainPresenterImpl;
import com.ipvans.flickrgallery.ui.main.MainViewState;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.observers.TestObserver;

import static java.util.Collections.emptyList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

public class MainPresenterTest {

    private Storage<Feed> storage;
    private MainPresenter<MainViewState> presenter;
    private TestObserver<MainViewState> observer;

    @Before
    public void before() {
        SchedulerProvider schedulers = new TestSchedulerProvider();
        storage = new MemoryStorage();
        presenter = new MainPresenterImpl(
                new FeedInteractorImpl(
                    new FeedRepositoryImpl(FlickrApiProvider.provideRestService(), storage),
                    schedulers
                ),
                schedulers);
        observer = TestObserver.create();
    }

    @Test
    public void getActualFeed() {
        presenter.onAttach();
        presenter.observe().subscribe(observer);

        presenter.search("", true);

        observer.awaitCount(1);
        observer.dispose();
        assertNotSame(null, observer.values().get(0).getData());
    }

    @Test
    public void getFeedFromStorage() {
        storage.save(new Feed("test", "", "", "", emptyList()));
        presenter.onAttach();
        presenter.observe().subscribe(observer);

        presenter.search("", false);

        observer.awaitCount(1);
        observer.dispose();
        assertEquals("test", observer.values().get(0).getData().getTitle());
    }

    @Test
    public void restorePresenterState() {
        Feed item = new Feed("test", "", "", "", emptyList());
        Feed item2 = new Feed("test2", "", "", "", emptyList());
        storage.save(item);

        presenter.onAttach();
        presenter.restoreState(new MainViewState(false, null, item2, ""));
        presenter.observe().subscribe(observer);

        observer.awaitCount(2);
        observer.dispose();
        assertEquals("test2", observer.values().get(0).getData().getTitle());
    }

}
