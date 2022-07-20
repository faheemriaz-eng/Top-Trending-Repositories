package com.faheem.sadapay.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.faheem.sadapay.R
import com.faheem.sadapay.databinding.ActivityGithubTrendingReposBinding
import com.faheem.sadapay.ui.adapter.GithubTrendingReposAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GithubTrendingRepoActivity : AppCompatActivity() {

    private lateinit var mViewBinding: ActivityGithubTrendingReposBinding

    private val viewModel: IGithubTrendingRepo by viewModels<GithubTrendingRepoVM>()

    @Inject
    lateinit var adapter: GithubTrendingReposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityGithubTrendingReposBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        initViews()
        addObservers()
    }

    private fun initViews() {
        initClickListeners()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mViewBinding.recyclerView.adapter = adapter
    }

    private fun initClickListeners() {
        mViewBinding.lyRetryView.buttonRetry.setOnClickListener {
            viewModel.loadTrendingRepositories(true)
        }
    }

    private fun bindViewState(viewState: GithubTrendingRepoVM.ViewState) {
        hideLoadingView()
        when (viewState) {
            is GithubTrendingRepoVM.ViewState.Loading -> showLoadingView()

            is GithubTrendingRepoVM.ViewState.ReposLoaded -> {
                if (!(viewState.repos.isNullOrEmpty())) {
                    adapter.setList(viewState.repos)
                    showDataView(true)
                } else {
                    showDataView(false)
                }
            }

            is GithubTrendingRepoVM.ViewState.ReposLoadFailure -> {
                showDataView(false)
            }
        }
    }

    private fun showLoadingView() {
        mViewBinding.lyLoadingView.shimmerFrameLayout.visibility = VISIBLE
        mViewBinding.recyclerView.visibility = GONE
        mViewBinding.lyRetryView.layoutError.visibility = GONE
    }

    private fun showDataView(show: Boolean) {
        mViewBinding.lyRetryView.layoutError.visibility = if (show) GONE else VISIBLE
        mViewBinding.recyclerView.visibility = if (show) VISIBLE else GONE
    }

    private fun hideLoadingView() {
        mViewBinding.lyLoadingView.shimmerFrameLayout.visibility = GONE
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