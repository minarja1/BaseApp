package cz.weissar.base.data.rest.ws

import cz.weissar.base.data.rest.dto.response.youtube.YouTubeVideosResponse
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApiService {

    companion object {
        operator fun invoke(retrofit: Retrofit): YoutubeApiService {
            return retrofit
                .create(YoutubeApiService::class.java)
        }
    }


//    https://www.googleapis.com/youtube/v3/videos?part=snippet%2CcontentDetails%2Cstatistics&chart=mostPopular&regionCode=US&key=[YOUR_API_KEY]' \
//    kategorie zde https://gist.github.com/dgp/1b24bf2961521bd75d6c

    @GET("videos")
    suspend fun getYouTubeVideoList(
        @Query("part") part: String,
        @Query("chart") chart: String,
        @Query("regionCode") regionCode: String,
        @Query("videoCategoryId") videoCategoryId: String,
        @Query("key") apiKey: String,
        @Query("pageToken") pageToken: String? = null,
        @Query("maxResults") maxResults: Int
    ): YouTubeVideosResponse

}