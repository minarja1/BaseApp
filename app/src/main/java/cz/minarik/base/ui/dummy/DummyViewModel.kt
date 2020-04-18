package cz.minarik.base.ui.dummy

import androidx.lifecycle.MutableLiveData
import cz.minarik.base.data.rest.dto.response.DummyResponse
import cz.minarik.base.di.base.BaseViewModel
import cz.minarik.base.di.repositories.DummyRepository

class DummyViewModel(private val dummyRepo: DummyRepository) : BaseViewModel() {

    val schedule = MutableLiveData<List<DummyResponse>>()

    fun getOrLoadDummy() {
        launch {
            // if (alreadyDownloaded) dummyRepo.loadDummy()
            // else
            schedule.postValue(dummyRepo.getDummy())
        }
    }
}