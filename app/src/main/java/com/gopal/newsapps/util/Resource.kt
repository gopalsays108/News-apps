package com.gopal.newsapps.util

//TO wrap around our network responses. It is generic class. it is usefull to differenciate successfull and failed state.
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}