package cz.weissar.base.data

import java.lang.Exception

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status,
    val message: String? = null) {
    companion object {
        val SUCCESS =
            NetworkState(Status.SUCCESS)
        val LOADING =
            NetworkState(Status.RUNNING)
        fun error(msg: String?) = NetworkState(
            Status.FAILED,
            msg
        )
        fun error(exception: Exception) = NetworkState(
            Status.FAILED,
            exception.message
        )
    }
}