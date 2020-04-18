package cz.minarik.base.di.repositories

import cz.minarik.base.data.db.dao.DummyDao
import cz.minarik.base.di.base.BaseRepository
import org.koin.core.inject

class DummyRepository : BaseRepository() {

    private val dummyDao by inject<DummyDao>()

    /**
     * @sample get for REST calls
     */
    suspend fun getDummy() = dummyWebService.getDummyResponseList()

    /**
     * @sample load for DB calls
     */
    suspend fun loadDummy() = dummyDao.getAll()
}