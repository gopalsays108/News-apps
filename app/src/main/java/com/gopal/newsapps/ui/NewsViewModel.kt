package com.gopal.newsapps.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gopal.newsapps.NewsApplication
import com.gopal.newsapps.NewsRepository
import com.gopal.newsapps.model.Article
import com.gopal.newsapps.model.NewsResponse
import com.gopal.newsapps.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    app: Application,
    val newsRepo: NewsRepository
) : AndroidViewModel(app) {
    var breakingNewPage = 1
    var searchNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null
    var searchNewsResponse: NewsResponse? = null
    val breakingNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    init {
        getBreakingNews("in")
    }

    public fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewCall(searchQuery)
    }

    private fun handleBreakingNewResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticle = breakingNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    private fun searchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = result
                } else {
                    val oldArticle = searchNewsResponse?.articles
                    val newArticle = result.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(searchNewsResponse ?: result)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepo.upsert(article)
    }

    fun getSavedNews() = newsRepo.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepo.deleteArticle(article)
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNewsLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepo.getBreakingNews(countryCode, breakingNewPage)
                breakingNewsLiveData.postValue(handleBreakingNewResponse(response))
                return
            }
            breakingNewsLiveData.postValue(Resource.Error("No Internet Connection"))

        } catch (e: Exception) {
            when (e) {
                is IOException -> breakingNewsLiveData.postValue(Resource.Error("Network failure"))
                else -> breakingNewsLiveData.postValue(Resource.Error("Error ${e.message}"))
            }
        }
    }

    private suspend fun safeSearchNewCall(searchQuery: String) {
        searchNewsLiveData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepo.searchNews(searchQuery, searchNewsPage)
                searchNewsLiveData.postValue(searchNewsResponse(response))
                return
            }
            searchNewsLiveData.postValue(Resource.Error("No Internet Connection"))

        } catch (e: Exception) {
            when (e) {
                is IOException -> searchNewsLiveData.postValue(Resource.Error("Network failure"))
                else -> searchNewsLiveData.postValue(Resource.Error("Error ${e.message}"))
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<NewsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}