package com.gopal.newsapps.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gopal.newsapps.ui.NewsActivity
import com.gopal.newsapps.ui.NewsViewModel

open class BaseFragment : Fragment() {
    lateinit var viewModel: NewsViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
    }
}