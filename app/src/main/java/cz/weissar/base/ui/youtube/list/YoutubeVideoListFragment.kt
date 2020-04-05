package cz.weissar.base.ui.youtube.list

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import cz.weissar.base.R
import cz.weissar.base.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_youtube_video_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class YoutubeVideoListFragment : BaseFragment(R.layout.fragment_youtube_video_list) {

    override val viewModel by viewModel<YoutubeListViewModel>()
    private var loaded = false

    private val youtubeAdapter by lazy {
        YoutubeVideoAdapterBuilder(requireContext()) { video, _ ->

            video.id?.let {
                val action = YoutubeVideoListFragmentDirections.openVideoDetailAction(it)
                findNavController(this).navigate(action)
            }

        }.adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //init toolbar
        initToolbar()

        // init views
        recyclerView.adapter = youtubeAdapter
        // init observers
        viewModel.videos.observe {
            youtubeAdapter.submitList(it)
        }


        if (!loaded) {
            // load
            viewModel.loadYouTubeVideos()
            loaded = true
        }
    }

    private fun initToolbar() {
        val navController = findNavController(this)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)    }

}