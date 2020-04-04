package cz.weissar.base.ui.youtube

import androidx.lifecycle.MutableLiveData
import cz.weissar.base.common.apiKey
import cz.weissar.base.data.rest.dto.model.YoutubeVideo
import cz.weissar.base.data.rest.ws.YoutubeApiService
import cz.weissar.base.di.base.BaseViewModel

class YoutubeViewModel(private val ws: YoutubeApiService) : BaseViewModel() {

    val videos = MutableLiveData<List<YoutubeVideo>>()

    fun getOrLoadDummy() {
        launch {
            // if (alreadyDownloaded) dummyRepo.loadDummy()
            // else
            val youTubeVideoList = ws.getYouTubeVideoList(
                "snippet",
                "mostPopular",
                "US",
                "20",
                apiKey
            )

            videos.postValue(youTubeVideoList.toVideoList())
        }
    }
}