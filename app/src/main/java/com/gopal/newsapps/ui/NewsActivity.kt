package com.gopal.newsapps.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.gopal.newsapps.NewsRepository
import com.gopal.newsapps.R
import com.gopal.newsapps.databinding.ActivityNewsBinding
import com.gopal.newsapps.db.ArticleDatabase

class NewsActivity : AppCompatActivity() {

    private lateinit var newsBinding: ActivityNewsBinding
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsBinding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(newsBinding.root)
        setUpBottomNavigation()

        val repository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = ViewModelProviderFactory(application,repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
    }

    private fun setUpBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newNavHostFragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        newsBinding.bottomNavigationView.setupWithNavController(navController)
    }
}
