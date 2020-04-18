package cz.minarik.base.ui.youtube.list

import cz.minarik.base.data.db.repository.YouTubeVideoPagedRepository
import cz.minarik.base.di.base.BaseViewModel
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.concurrent.Executors

class YoutubeListViewModel : BaseViewModel() {

    val repository: YouTubeVideoPagedRepository by inject {
        parametersOf(
            Executors.newFixedThreadPool(
                5
            ), ioScope
        )
    }

    private val repoResult = repository.getYouTubeVideos()

    val videos = repoResult.pagedList
    val loadingAfter = repoResult.loadingAfter
    val loadingInitial = repoResult.loadingInitial

    fun refresh() {
        repoResult.refresh.invoke()
    }
}