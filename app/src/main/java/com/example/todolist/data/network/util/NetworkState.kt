package com.example.todolist.data.network.util

/**
 * Sealed class representing the network state.
 *
 * @param T type parameter representing the data type
 */
sealed class NetworkState <out T> {
    object Loading : NetworkState<Nothing>()
    data class OK(val revision: Int) : NetworkState<Nothing>()
    data class Success<T>(val data: T, val revision: Int) : NetworkState<T>()
    data class Failure(val cause: Throwable): NetworkState<Nothing>()

}
