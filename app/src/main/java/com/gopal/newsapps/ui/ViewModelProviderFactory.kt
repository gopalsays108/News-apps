package com.gopal.newsapps.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gopal.newsapps.NewsRepository

class ViewModelProviderFactory(
    val app: Application,
    val newsRepo: NewsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app,newsRepo) as T
    }
}