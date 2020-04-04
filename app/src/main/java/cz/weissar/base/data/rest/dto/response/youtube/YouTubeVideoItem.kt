package cz.weissar.base.data.rest.dto.response.youtube

import cz.weissar.base.data.rest.dto.model.YoutubeVideo

data class YouTubeVideoItem(
    val snippet: YouTubeSnippet? = null
) {
    fun toYoutubeVideo(): YoutubeVideo {
        return YoutubeVideo(
            snippet?.title,
            snippet?.description
        )
    }
}