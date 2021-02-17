package cz.minarik.base.di.base

import android.content.ComponentCallbacks
import android.content.res.Configuration
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

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

    protected fun launch(
        failure: ((Exception) -> Unit)? = null,
        operation: (suspend () -> Unit)
    ) {
        ioScope.launch {
            try {
                operation()
            } catch (e: Exception) {
                failure?.invoke(e)
            }
        }
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