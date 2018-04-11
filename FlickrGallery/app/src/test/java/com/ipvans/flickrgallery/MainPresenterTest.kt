package com.ipvans.flickrgallery

import com.ipvans.flickrgallery.data.model.Feed
import com.ipvans.flickrgallery.data.model.FeedItem
import com.ipvans.flickrgallery.data.repository.FeedRepositoryImpl
import com.ipvans.flickrgallery.data.source.storage.Storage
import com.ipvans.flickrgallery.domain.FeedInteractorImpl
import com.ipvans.flickrgallery.mock.FlickrApiProvider
import com.ipvans.flickrgallery.mock.MemoryStorage
import com.ipvans.flickrgallery.mock.TestSchedulerProvider
import com.ipvans.flickrgallery.ui.main.MainPresenter
import com.ipvans.flickrgallery.ui.main.MainPresenterImpl
import com.ipvans.flickrgallery.ui.main.MainViewState

import org.junit.Before
import org.junit.Test

import io.reactivex.observers.TestObserver

import java.util.Collections.emptyList
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotSame

class MainPresenterTest {

    private lateinit var storage: Storage<Feed>
    private lateinit var presenter: MainPresenter<MainViewState>
    private lateinit var observer: TestObserver<MainViewState>

    @Before
    fun before() {
        val schedulers = TestSchedulerProvider()
        storage = MemoryStorage()
        presenter = MainPresenterImpl(
            FeedInteractorImpl(
                FeedRepositoryImpl(FlickrApiProvider.provideRestService(), storage),
                schedulers
            ),
            schedulers
        )
        observer = TestObserver.create()
    }

    @Test
    fun getActualFeed() {
        presenter.onAttach()
        presenter.observe().subscribe(observer)

        presenter.search("", true)

        observer.awaitCount(1)
        observer.dispose()
        assertNotSame(null, observer.values()[0].data)
    }

    @Test
    fun getFeedFromStorage() {
        storage.save(Feed("test", "", "", "", emptyList<FeedItem>()))
        presenter.onAttach()
        presenter.observe().subscribe(observer)

        presenter.search("", false)

        observer.awaitCount(1)
        observer.dispose()
        assertEquals("test", observer.values()[0].data.title)
    }

    @Test
    fun restorePresenterState() {
        val item = Feed("test", "", "", "", emptyList<FeedItem>())
        val item2 = Feed("test2", "", "", "", emptyList<FeedItem>())
        storage.save(item)

        presenter.onAttach()
        presenter.restoreState(MainViewState(false, null, item2, ""))
        presenter.observe().subscribe(observer)

        observer.awaitCount(2)
        observer.dispose()
        assertEquals("test2", observer.values()[0].data.title)
    }

}
