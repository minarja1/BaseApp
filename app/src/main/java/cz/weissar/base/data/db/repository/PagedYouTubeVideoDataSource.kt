package cz.weissar.base.data.db.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import cz.weissar.base.common.apiKey
import cz.weissar.base.data.NetworkState
import cz.weissar.base.data.rest.dto.model.YouTubeVideo
import cz.weissar.base.data.rest.ws.YoutubeApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class PagedYouTubeVideoDataSource(
    private val apiService: YoutubeApiService,
    private val scope: CoroutineScope,
    private val retryExecutor: Executor
) : PageKeyedDataSource<String, YouTubeVideo>() {

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, YouTubeVideo>
    ) {
        // ignored, since we only ever append to our initial load
    }

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, YouTubeVideo>
    ) {
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)
        scope.launch {
            try {
                val response = apiService.getYouTubeVideoList(
                    "snippet",
                    "mostPopular",
                    "US",
                    "20",
                    apiKey,
                    maxResults = params.requestedLoadSize
                )


                retry = null
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
                callback.onResult(
                    response.toVideoList(),
                    response.prevPageToken,
                    response.nextPageToken
                )
            } catch (exception: java.lang.Exception) {
                //todo handle errors properly
                retry = {
                    loadInitial(params, callback)
                }
                val error = NetworkState.error(exception.message ?: "unknown error")
                networkState.postValue(error)
                initialLoad.postValue(error)
            }
        }

    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, YouTubeVideo>
    ) {
        networkState.postValue(NetworkState.LOADING)
        scope.launch {
            try {
                val response = apiService.getYouTubeVideoList(
                    "snippet",
                    "mostPopular",
                    "US",
                    "20",
                    apiKey,
                    pageToken = params.key,
                    maxResults = params.requestedLoadSize
                )
                retry = null
                callback.onResult(response.toVideoList(), response.nextPageToken)

            } catch (e: Exception) {
                //todo handle errors properly
                retry = {
                    loadAfter(params, callback)
                }
                networkState.postValue(NetworkState.error(e.message ?: "unknown err"))
            }

        }
    }

}