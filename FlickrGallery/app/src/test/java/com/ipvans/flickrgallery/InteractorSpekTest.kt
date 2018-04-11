package com.ipvans.flickrgallery

import com.ipvans.flickrgallery.data.model.Feed
import com.ipvans.flickrgallery.data.model.FeedItem
import com.ipvans.flickrgallery.data.repository.FeedRepositoryImpl
import com.ipvans.flickrgallery.data.source.storage.Storage
import com.ipvans.flickrgallery.domain.FeedInteractorImpl
import com.ipvans.flickrgallery.domain.UpdateEvent
import com.ipvans.flickrgallery.domain.model.Response
import com.ipvans.flickrgallery.mock.FlickrApiProvider
import com.ipvans.flickrgallery.mock.MemoryStorage
import com.ipvans.flickrgallery.mock.TestSchedulerProvider
import io.reactivex.observers.TestObserver
import junit.framework.Assert.assertEquals
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.Collections


class InteractorSpekTest : Spek({

    var observer: TestObserver<Response<Feed>> = TestObserver.create()
    fun feed(): Response<Feed> = observer.values().last()

    fun disposeObserver() {
        observer.dispose()
        observer.assertNoErrors()
    }

    fun Storage<Feed>.saveMockData() {
        val item = Feed("test title", "", "", "", Collections.emptyList<FeedItem>())
        save(item)
    }

    given("empty memory storage") {
        val storage = MemoryStorage()
        val schedulerProvider = TestSchedulerProvider()
        val interactor = FeedInteractorImpl(
            FeedRepositoryImpl(FlickrApiProvider.provideRestService(), storage),
            schedulerProvider
        )

        beforeEachTest {
            observer = TestObserver.create()
            interactor.observe()
                .filter { it -> !it.isLoading }
                .doOnNext { println(it) }
                .subscribe(observer)
        }

        afterEachTest { disposeObserver() }

        on("updating feed") {
            interactor.getFeed(UpdateEvent("", true))
            it("should complete without errors") {
                assertEquals(Throwable(), feed().error)
            }
        }

        context("some data stored to cache") {
            on("updating feed from cache") {
                storage.saveMockData()
                interactor.getFeed(UpdateEvent("", false))
                it("should return stored data") {
                    assertEquals("test title1", feed().data.title)
                }
            }
            on("updating feed (force update)") {
                interactor.getFeed(UpdateEvent("", true))
                it("should fetch data from api") {
                    assertEquals("Uploads from everyone1", feed().data.title)
                }
            }
        }
    }

})