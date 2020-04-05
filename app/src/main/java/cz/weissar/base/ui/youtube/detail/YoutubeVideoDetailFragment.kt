package cz.weissar.base.ui.youtube.detail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import cz.weissar.base.R
import cz.weissar.base.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_youtube_video_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class YoutubeVideoDetailFragment : BaseFragment(R.layout.fragment_youtube_video_detail) {

    private val args: YoutubeVideoDetailFragmentArgs by navArgs()

    override val viewModel by viewModel<YoutubeDetailViewModel>()
    //private val viewModel2 by sharedViewModel<ScheduleViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()

        videoIdTextView.text = args.videoId
    }

    private fun initToolbar() {
        val navController = NavHostFragment.findNavController(this)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}