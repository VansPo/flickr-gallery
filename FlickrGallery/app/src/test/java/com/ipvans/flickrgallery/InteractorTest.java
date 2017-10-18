package com.ipvans.flickrgallery;

import com.ipvans.flickrgallery.data.model.Feed;
import com.ipvans.flickrgallery.data.repository.FeedRepositoryImpl;
import com.ipvans.flickrgallery.data.source.storage.Storage;
import com.ipvans.flickrgallery.domain.FeedInteractor;
import com.ipvans.flickrgallery.domain.FeedInteractorImpl;
import com.ipvans.flickrgallery.domain.UpdateEvent;
import com.ipvans.flickrgallery.domain.model.Response;
import com.ipvans.flickrgallery.mock.FlickrApiProvider;
import com.ipvans.flickrgallery.mock.MemoryStorage;
import com.ipvans.flickrgallery.mock.TestSchedulerProvider;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.observers.TestObserver;

import static java.util.Collections.emptyList;
import static junit.framework.Assert.assertEquals;

public class InteractorTest {

    private Storage<Feed> storage;
    private FeedInteractor interactor;
    private TestObserver<Response<Feed>> observer;

    @Before
    public void before() {
        storage = new MemoryStorage();
        interactor = new FeedInteractorImpl(
                new FeedRepositoryImpl(FlickrApiProvider.provideRestService(), storage),
                new TestSchedulerProvider());
        observer = TestObserver.create();
    }

    @Test
    public void getActualFeed() {
        interactor.observe()
                .filter(it -> !it.isLoading())
                .subscribe(observer);

        interactor.getFeed(new UpdateEvent("", true));

        observer.awaitCount(1);
        observer.dispose();
        observer.assertNoErrors();
        assertEquals(null, ((Response<Feed>)observer.getEvents().get(0).get(0)).getError());
    }

    @Test
    public void getFromCache() {
        saveMockData();

        interactor.observe()
                .filter(it -> !it.isLoading())
                .subscribe(observer);

        interactor.getFeed(new UpdateEvent("", false));

        observer.awaitCount(1);
        observer.dispose();
        observer.assertNoErrors();
        Feed feed = ((Response<Feed>)observer.getEvents().get(0).get(0)).getData();
        assertEquals("test title", feed.getTitle());
    }

    @Test
    public void getActualDataAndSave() {
        interactor.observe()
                .filter(it -> !it.isLoading())
                .subscribe(observer);

        interactor.getFeed(new UpdateEvent("", false));

        observer.awaitCount(1);
        observer.dispose();
        observer.assertNoErrors();

        assertEquals(true, storage.get().blockingGet().isPresent());
    }

    private void saveMockData() {
        Feed item = new Feed("test title", "", "", "", emptyList());
        storage.save(item);
    }

}
