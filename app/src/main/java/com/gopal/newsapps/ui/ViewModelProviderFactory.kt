package com.gopal.newsapps.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gopal.newsapps.NewsRepository

class ViewModelProviderFactory(
    val newsRepo: NewsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepo) as T
    }
}