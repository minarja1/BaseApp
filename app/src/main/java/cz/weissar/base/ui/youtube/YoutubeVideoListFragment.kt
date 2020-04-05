package cz.weissar.base.ui.youtube

import android.os.Bundle
import android.view.View
import cz.weissar.base.R
import cz.weissar.base.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_youtube_video_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class YoutubeVideoListFragment : BaseFragment(R.layout.fragment_youtube_video_list) {

    override val viewModel by viewModel<YoutubeViewModel>()
    //private val viewModel2 by sharedViewModel<ScheduleViewModel>()

    private val youtubeAdapter by lazy {
        YoutubeVideoAdapterBuilder(requireContext()) {
            // TODO on click
        }.adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init views
        recyclerView.adapter = youtubeAdapter


        // init observers
        viewModel.videos.observe {
            youtubeAdapter.submitList(it)
        }

        // load
        viewModel.loadYouTubeVideos()
    }
}