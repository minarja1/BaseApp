package cz.weissar.base.ui.youtube.list

import androidx.lifecycle.MutableLiveData
import cz.weissar.base.common.apiKey
import cz.weissar.base.data.db.repository.YouTubeVideoPagedRepository
import cz.weissar.base.data.rest.dto.model.YouTubeVideo
import cz.weissar.base.data.rest.ws.YoutubeApiService
import cz.weissar.base.di.base.BaseViewModel
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.concurrent.Executors

class YoutubeListViewModel(
    private val ws: YoutubeApiService
) : BaseViewModel() {

    val repository: YouTubeVideoPagedRepository by inject {
        parametersOf(
            Executors.newFixedThreadPool(
                5
            ), scope
        )
    }

//    val videos = MutableLiveData<List<YouTubeVideo>>()
    private var loaded = false

    private val repoResult = repository.getYouTubeVideos()

    val videos = repoResult.pagedList
    val networkState = repoResult.networkState
    val refreshState = repoResult.refreshState

    fun refresh() {
        repoResult.refresh.invoke()
    }

    fun loadYouTubeVideos() {
//        if (!loaded) {
//            launch {
//                // if (alreadyDownloaded) dummyRepo.loadDummy()
//                // else
//                val youTubeVideoList = ws.getYouTubeVideoList(
//                    "snippet",
//                    "mostPopular",
//                    "US",
//                    "20",
//                    apiKey,
//                    maxResults = 25
//                )
//
//                loaded = true
//                videos.postValue(youTubeVideoList.toVideoList())
//            }
//        }
    }
}