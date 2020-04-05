package cz.weissar.base.ui.youtube.detail

import androidx.lifecycle.MutableLiveData
import cz.weissar.base.common.apiKey
import cz.weissar.base.data.rest.dto.model.YoutubeVideo
import cz.weissar.base.data.rest.ws.YoutubeApiService
import cz.weissar.base.di.base.BaseViewModel

class YoutubeDetailViewModel(private val ws: YoutubeApiService) : BaseViewModel() {

}