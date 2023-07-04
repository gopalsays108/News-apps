package com.gopal.newsapps

import com.gopal.newsapps.api.RetrofitInstance
import com.gopal.newsapps.db.ArticleDatabase
import com.gopal.newsapps.model.Article

class NewsRepository(
    val db: ArticleDatabase
) {

    suspend fun getBreakingNews(countryCode: String, pageNo: Int) =
        RetrofitInstance.api.getBreakingNew(countryCode, pageNo)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.search(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticle()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}