package cz.weissar.base.di.base

import android.content.ComponentCallbacks
import android.content.res.Configuration
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.weissar.base.data.NetworkState
import kotlinx.coroutines.*
import retrofit2.HttpException

abstract class BaseViewModel : ViewModel(), ComponentCallbacks {
    private val uiJob = SupervisorJob()
    private val ioJob = SupervisorJob()
    private val defaultJob = SupervisorJob()

    private val handler = CoroutineExceptionHandler { _, ex -> ex.printStackTrace() }

    /**
     * Use this dispatcher to run a coroutine on the main Android thread.
     * This should be used only for interacting with the UI and performing quick work.
     * Examples include calling suspend functions, running Android UI framework operations, and updating LiveData objects.
     */
    protected val uiScope = CoroutineScope(Dispatchers.Main + uiJob + handler)

    /**
     * This dispatcher is optimized to perform disk or network I/O outside of the main thread.
     * Examples include using the Room component, reading from or writing to files, and running any network operations.
     */
    protected val ioScope = CoroutineScope(Dispatchers.IO + ioJob + handler)

    /**
     * This dispatcher is optimized to perform CPU-intensive work outside of the main thread.
     * Example use cases include sorting a list and parsing JSON.
     */
    protected val defaultScope = CoroutineScope(Dispatchers.Default + defaultJob + handler)

    val state = MutableLiveData<NetworkState>()

    protected fun launch(
        failure: ((Exception) -> Unit)? = null,
        defaultState: MutableLiveData<NetworkState>? = state,
        operation: (suspend () -> Unit)
    ) {
        ioScope.launch {
            defaultState?.postValue(NetworkState.LOADING)
            try {
                operation()
                defaultState?.postValue(NetworkState.SUCCESS)
            } catch (e: Exception) {
                if (e is HttpException) {
                    // ToDo - možno reagovat na http exception
                }
                failure?.invoke(e)
                defaultState?.postValue(NetworkState.error(e))
            }
        }
    }

    /**
     * How to call launch from children
     */
    private fun test() {
        launch {

        }

        launch(
            operation = {

            },
            failure = {

            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        uiJob.complete()
        ioJob.cancel()
        defaultJob.cancel()
        ioScope.cancel()
        uiScope.cancel()
        defaultScope.cancel()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {

    }

    override fun onLowMemory() {

    }
}