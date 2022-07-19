package com.faheem.sadapay.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.faheem.sadapay.databinding.ActivityGithubTrendingReposBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GithubTrendingRepoActivity : AppCompatActivity() {

    lateinit var mViewBinding: ActivityGithubTrendingReposBinding

    private val viewModel: GithubTrendingRepoVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityGithubTrendingReposBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)

        viewModel.loadTrendingRepositories()
    }

}