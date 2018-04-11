package com.ipvans.flickrgallery

import com.ipvans.flickrgallery.data.model.Feed
import com.ipvans.flickrgallery.data.model.FeedItem
import com.ipvans.flickrgallery.data.repository.FeedRepositoryImpl
import com.ipvans.flickrgallery.data.source.storage.Storage
import com.ipvans.flickrgallery.domain.FeedInteractor
import com.ipvans.flickrgallery.domain.FeedInteractorImpl
import com.ipvans.flickrgallery.domain.UpdateEvent
import com.ipvans.flickrgallery.domain.model.Response
import com.ipvans.flickrgallery.mock.FlickrApiProvider
import com.ipvans.flickrgallery.mock.MemoryStorage
import com.ipvans.flickrgallery.mock.TestSchedulerProvider

import org.junit.Before
import org.junit.Test

import io.reactivex.observers.TestObserver

import java.util.Collections.emptyList
import junit.framework.Assert.assertEquals

class InteractorTest {

    private lateinit var storage: Storage<Feed>
    private lateinit var interactor: FeedInteractor
    private lateinit var observer: TestObserver<Response<Feed>>

    @Before
    fun before() {
        storage = MemoryStorage()
        interactor = FeedInteractorImpl(
            FeedRepositoryImpl(FlickrApiProvider.provideRestService(), storage),
            TestSchedulerProvider()
        )
        observer = TestObserver.create()
    }

    @Test
    fun getActualFeed() {
        interactor.observe()
            .filter { it -> !it.isLoading }
            .subscribe(observer)

        interactor.getFeed(UpdateEvent("", true))

        observer.awaitCount(1)
        observer.dispose()
        observer.assertNoErrors()
        assertEquals(null, observer.values()[0].error)
    }

    @Test
    fun getFromCache() {
        saveMockData()

        interactor.observe()
            .filter { it -> !it.isLoading }
            .subscribe(observer)

        interactor.getFeed(UpdateEvent("", false))

        observer.awaitCount(1)
        observer.dispose()
        observer.assertNoErrors()
        val feed = observer.values()[0].data
        assertEquals("test title", feed.title)
    }

    @Test
    fun getActualDataAndSave() {
        interactor.observe()
            .filter { it -> !it.isLoading }
            .subscribe(observer)

        interactor.getFeed(UpdateEvent("", false))

        observer.awaitCount(1)
        observer.dispose()
        observer.assertNoErrors()

        assertEquals(true, storage.get().blockingGet().isPresent)
    }

    private fun saveMockData() {
        val item = Feed("test title", "", "", "", emptyList<FeedItem>())
        storage.save(item)
    }

}
