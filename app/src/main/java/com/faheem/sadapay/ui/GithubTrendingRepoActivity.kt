package com.faheem.sadapay.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.faheem.sadapay.R
import com.faheem.sadapay.data.dtos.Item
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
        viewModel.loadTrendingRepositories()
    }

    private fun bindListData(repos: List<Item>) {
        if (!(repos.isNullOrEmpty())) {
            adapter.setList(repos)
        } else {
        }
    }

    private fun addObservers() {
        viewModel.trendingRepos.observe(this, ::bindListData)
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