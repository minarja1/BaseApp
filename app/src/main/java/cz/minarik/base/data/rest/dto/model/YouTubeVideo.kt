package cz.minarik.base.data.rest.dto.model

import java.io.Serializable

data class YouTubeVideo(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val maxResThumbnailUrl: String? = null
) : Serializable