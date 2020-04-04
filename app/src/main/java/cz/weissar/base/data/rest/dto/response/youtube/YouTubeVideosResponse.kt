package cz.weissar.base.data.rest.dto.response.youtube

import cz.weissar.base.data.rest.dto.model.YoutubeVideo

data class YouTubeVideosResponse(

    val kind: String? = null,
    val etag: String? = null,
    val nextPageToken: String? = null,
    val prevPageToken: String? = null,
    val pageInfo: YouTubePageInfo? = null,
    val items: List<YouTubeVideoItem?>? = null
) {
    fun toVideoList(): MutableList<YoutubeVideo> {
        val result = mutableListOf<YoutubeVideo>()
        if (items.isNullOrEmpty()) return result
        for (item in items) {
            item?.let {
                result.add(it.toYoutubeVideo())
            }
        }
        return result
    }
}