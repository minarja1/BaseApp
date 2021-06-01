package cz.minarik.base.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import cz.minarik.base.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseFragment(@LayoutRes private val layoutId: Int) :
    Fragment(R.layout.fragment_base) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        //(view.container as FrameLayout)
        view?.findViewById<FrameLayout>(R.id.container).run {
            LayoutInflater.from(requireContext()).inflate(layoutId, this, true)
        }
        return view
    }

    protected fun <T> LiveData<T>.observe(function: (value: T) -> Unit) {
        this.observe(viewLifecycleOwner, Observer { function(it) })
    }

    /**
     * refer to https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda
     */
    protected fun <T> Flow<T>.collectWhenStarted(function: (value: T) -> Unit) {
        this.let { flow ->
            // Create a new coroutine since repeatOnLifecycle is a suspend function
            lifecycleScope.launch {
                // The block passed to repeatOnLifecycle is executed when the lifecycle
                // is at least STARTED and is cancelled when the lifecycle is STOPPED.
                // It automatically restarts the block when the lifecycle is STARTED again.
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    // Safely collect from locationFlow when the lifecycle is STARTED
                    // and stops collection when the lifecycle is STOPPED
                    flow.collect {
                        function.invoke(it)
                    }
                }
            }
        }
    }

}