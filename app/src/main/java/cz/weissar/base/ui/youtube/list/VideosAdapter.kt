package cz.weissar.base.ui.youtube.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import cz.weissar.base.R
import cz.weissar.base.data.rest.dto.model.YouTubeVideo

class VideosAdapter(private var onItemClicked: (video: YouTubeVideo, imageView: ImageView) -> Unit) :
    PagedListAdapter<YouTubeVideo, RecyclerView.ViewHolder>(diffCallback) {

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<YouTubeVideo>() {

            override fun areItemsTheSame(oldItem: YouTubeVideo, newItem: YouTubeVideo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: YouTubeVideo, newItem: YouTubeVideo): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VideoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_youtube_video, parent, false)
        )
    }

    class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.title)
        private val imageView: ImageView = view.findViewById(R.id.imageView)
        private val videoBackGround: ViewGroup = view.findViewById(R.id.videoBackGround)

        fun bind(
            video: YouTubeVideo?,
            onItemClicked: (video: YouTubeVideo, imageView: ImageView) -> Unit
        ) {
            if (video == null) return
            title.text = video.title
            imageView.load(video.maxResThumbnailUrl) {
                crossfade(true)
            }
            imageView.transitionName = video.maxResThumbnailUrl

            videoBackGround.setOnClickListener {
                onItemClicked(video, imageView)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VideoViewHolder).bind(getItem(position), onItemClicked)
    }
}