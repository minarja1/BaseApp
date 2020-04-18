package cz.minarik.base.ui.youtube.detail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import coil.api.load
import cz.minarik.base.R
import cz.minarik.base.common.extensions.initToolbar
import cz.minarik.base.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_youtube_video_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class YoutubeVideoDetailFragment : BaseFragment(R.layout.fragment_youtube_video_detail) {

    private val args: YoutubeVideoDetailFragmentArgs by navArgs()

    override val viewModel by viewModel<YoutubeDetailViewModel>()
    //private val viewModel2 by sharedViewModel<ScheduleViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(toolbar)

        val video = args.video
        toolbar.title = video.title

        detailImageView.transitionName = video.maxResThumbnailUrl
        detailImageView.load(video.maxResThumbnailUrl)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

}