package cz.weissar.base.ui.youtube.list

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment.findNavController
import cz.weissar.base.R
import cz.weissar.base.common.extensions.initToolbar
import cz.weissar.base.data.NetworkState
import cz.weissar.base.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_youtube_video_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class YoutubeVideoListFragment : BaseFragment(R.layout.fragment_youtube_video_list) {

    override val viewModel by viewModel<YoutubeListViewModel>()

    private val youtubeAdapter by lazy {
        VideosAdapter { video, imageView ->
            val action = YoutubeVideoListFragmentDirections.openVideoDetailAction(
                video
            )
            video.maxResThumbnailUrl?.let {
                val extras = FragmentNavigatorExtras(
                    imageView to it
                )
                findNavController(this).navigate(action, extras)
            } ?: findNavController(this).navigate(action)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        initObservers()

        initSwipeToRefresh()
    }

    private fun initObservers() {
        viewModel.videos.observe {
            youtubeAdapter.submitList(it)
        }
    }

    private fun initViews() {
        initToolbar(toolbar)
        recyclerView.adapter = youtubeAdapter
    }

    private fun initSwipeToRefresh() {
        viewModel.loadingInitial.observe {
            swipe_refresh.isRefreshing = it == NetworkState.LOADING
        }

        swipe_refresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

}