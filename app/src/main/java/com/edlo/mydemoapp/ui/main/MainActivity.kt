package com.edlo.mydemoapp.ui.main

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import com.edlo.mydemoapp.databinding.ActivityMainBinding
import com.edlo.mydemoapp.helper.DialogHelper
import com.edlo.mydemoapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Inject lateinit var dialogHelper: DialogHelper

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: GithubUsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()

        changeFragment(GithubUsersFragment::class.java)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this).get(GithubUsersViewModel::class.java)
    }

    override fun addDisposable() {
        disposable.addAll(
            subscribeLoading(),
            subscribeSearchUser(),
            subscribeSelectedUser()
        )
    }

    private fun subscribeLoading(): Disposable {
        return viewModel.onLoading.subscribe { isLoading ->
            if(isLoading) {
                dialogHelper.showProgressDialog(this)
            } else {
                dialogHelper.hideProgressDialog(this)
            }
        }
    }

    private fun subscribeSearchUser(): Disposable {
        return viewModel.onSearchUser.subscribe { key ->
            hideKeyboard(binding.root.windowToken)
            viewModel.listGitHubUsers(key)
        }
    }

    private fun subscribeSelectedUser(): Disposable {
        return viewModel.onSelectedUser.subscribe { user ->
           pushFragment(GithubUserDetailFragment::class.java)
        }
    }

    fun hideKeyboard(token: IBinder?): Boolean {
        if (token != null) {
            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return im.hideSoftInputFromWindow(
                token,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
        return false
    }
}