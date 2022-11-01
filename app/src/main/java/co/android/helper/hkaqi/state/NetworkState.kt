package co.android.helper.hkaqi.state

sealed class NetworkState<out T> {
    class Loading<T> : NetworkState<T>()
    data class Success<out T>(val data: T) : NetworkState<T>()
    data class Failure<T>(val error: String) : NetworkState<T>()
}