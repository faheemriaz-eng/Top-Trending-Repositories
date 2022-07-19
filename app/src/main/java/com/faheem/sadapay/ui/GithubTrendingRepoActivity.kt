package com.faheem.sadapay.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.faheem.sadapay.R
import com.faheem.sadapay.databinding.ActivityGithubTrendingReposBinding
import com.faheem.sadapay.ui.adapter.GithubTrendingReposAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GithubTrendingRepoActivity : AppCompatActivity() {

    lateinit var mViewBinding: ActivityGithubTrendingReposBinding

    private val viewModel: GithubTrendingRepoVM by viewModels()

    @Inject
    lateinit var adapter: GithubTrendingReposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityGithubTrendingReposBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)

        mViewBinding.recyclerView.adapter = adapter
        addObservers()
    }

    private fun bindViewState(viewState: GithubTrendingRepoVM.ViewState) {
        when (viewState) {
            is GithubTrendingRepoVM.ViewState.Loading -> {
            }
            is GithubTrendingRepoVM.ViewState.ReposLoaded -> {
                mViewBinding.lyRetryView.layoutError.visibility = View.GONE
                mViewBinding.recyclerView.visibility = View.VISIBLE
                adapter.setList(viewState.repos ?: listOf())
            }

            is GithubTrendingRepoVM.ViewState.ReposLoadFailure -> {
                mViewBinding.recyclerView.visibility = View.GONE
                mViewBinding.lyRetryView.layoutError.visibility = View.VISIBLE
            }
        }
    }

    private fun addObservers() {
        viewModel.viewState.observe(this, ::bindViewState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_refresh, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> viewModel.loadTrendingRepositories(refresh = true)
        }
        return super.onOptionsItemSelected(item)
    }

}