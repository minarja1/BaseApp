package cz.weissar.base.ui.youtube.list

import android.content.Context
import android.view.View
import coil.api.load
import cz.weissar.base.R
import cz.weissar.base.data.rest.dto.model.YouTubeVideo
import cz.weissar.base.ui.base.BaseListAdapterBuilder
import kotlinx.android.synthetic.main.row_youtube_video.view.*


class YoutubeVideoAdapterBuilder(
    context: Context,
    private val onClick: (YouTubeVideo, view: View) -> Unit
) :
    BaseListAdapterBuilder<YouTubeVideo>(context) {

    override fun getRowLayout() = R.layout.row_youtube_video

    override fun View.onBind(item: YouTubeVideo) {
        title.text = item.title
        imageView.load(item.maxResThumbnailUrl) {
            crossfade(true)
        }
        imageView.transitionName = item.maxResThumbnailUrl

        videoBackGround.setOnClickListener {
            onClick(item, imageView)
        }
    }
}