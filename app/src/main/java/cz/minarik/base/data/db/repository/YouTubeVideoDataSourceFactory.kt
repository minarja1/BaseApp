package cz.minarik.base.data.db.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import cz.minarik.base.data.rest.dto.model.YouTubeVideo
import cz.minarik.base.data.rest.ws.YoutubeApiService
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.Executor

/**
 * A simple data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its network request status etc back to the UI. See the PagedListWithCallbacks creation
 * in the Repository class.
 */
class YouTubeVideoDataSourceFactory(
    private val apiService: YoutubeApiService,
    private val scope: CoroutineScope,
    private val networkExecutor: Executor
) : DataSource.Factory<String, YouTubeVideo>() {
    val sourceLiveData = MutableLiveData<PagedYouTubeVideoDataSource>()
    override fun create(): DataSource<String, YouTubeVideo> {
        val source = PagedYouTubeVideoDataSource(apiService, scope, networkExecutor)
        sourceLiveData.postValue(source)
        return source
    }
}

