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
            if (video.id != null && video.maxResThumbnailUrl != null) {
                val action = YoutubeVideoListFragmentDirections.openVideoDetailAction(
                    video.id,
                    video.maxResThumbnailUrl
                )
                val extras = FragmentNavigatorExtras(
                    imageView to video.maxResThumbnailUrl
                )

                findNavController(this).navigate(action, extras)
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(toolbar)

        // init views
        recyclerView.adapter = youtubeAdapter

        // init observers
        viewModel.videos.observe {
            youtubeAdapter.submitList(it)
        }

        initSwipeToRefresh()
    }

    private fun initSwipeToRefresh() {
        viewModel.refreshState.observe {
            swipe_refresh.isRefreshing = it == NetworkState.LOADING
        }

        swipe_refresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

}