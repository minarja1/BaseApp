package cz.weissar.base.data.rest.dto.response.youtube

import cz.weissar.base.data.rest.dto.model.YoutubeVideo

data class YouTubeVideoItem(
    val id: String? = null,
    val snippet: YouTubeSnippet? = null
) {
    fun toYoutubeVideo(): YoutubeVideo {
        return YoutubeVideo(
            id,
            snippet?.title,
            snippet?.description,
            snippet?.thumbnails?.standard?.url
        )
    }
}