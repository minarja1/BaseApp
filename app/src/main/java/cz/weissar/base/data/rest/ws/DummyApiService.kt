package cz.weissar.base.data.rest.ws

import cz.weissar.base.data.rest.dto.response.DummyResponse
import retrofit2.Retrofit
import retrofit2.http.GET

interface DummyApiService {

    companion object {
        operator fun invoke(retrofit: Retrofit): DummyApiService {
            return retrofit
                .create(DummyApiService::class.java)
        }
    }

    @GET("posts")
    suspend fun getDummyResponseList(): List<DummyResponse>

}