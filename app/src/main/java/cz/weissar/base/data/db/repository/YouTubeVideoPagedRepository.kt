/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.weissar.base.data.db.repository

import androidx.annotation.MainThread
import androidx.lifecycle.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import cz.weissar.base.common.pageSize
import cz.weissar.base.data.PagedListWithCallbacks
import cz.weissar.base.data.rest.dto.model.YouTubeVideo
import cz.weissar.base.data.rest.ws.YoutubeApiService
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.Executor

/**
 * Repository implementation that returns a PagedListWithCallbacks that loads data directly from network by using
 * the previous / next page keys returned in the query.
 */
class YouTubeVideoPagedRepository(
    private val apiService: YoutubeApiService,
    private val networkExecutor: Executor,
    private val scope: CoroutineScope
) {
    private fun pagedListConfig() = PagedList.Config.Builder()
        .setInitialLoadSizeHint(pageSize)
        .setEnablePlaceholders(true)
        .setPageSize(pageSize)
        .setPrefetchDistance(pageSize/2)
        .build()

    @MainThread
    fun getYouTubeVideos(): PagedListWithCallbacks<YouTubeVideo> {
        val sourceFactory = YouTubeVideoDataSourceFactory(apiService, scope, networkExecutor)

        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val livePagedList = LivePagedListBuilder(sourceFactory, pagedListConfig()).setFetchExecutor(networkExecutor).build()

        val loadingInitial = sourceFactory.sourceLiveData.switchMap {
            it.loadingInitial
        }

        return PagedListWithCallbacks(
            pagedList = livePagedList,
            loadingAfter = sourceFactory.sourceLiveData.switchMap {
                it.loadingAfter
            },
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            },
            loadingInitial = loadingInitial
        )
    }
}

