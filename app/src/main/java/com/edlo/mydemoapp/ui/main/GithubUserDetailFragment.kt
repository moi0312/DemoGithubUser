package com.edlo.mydemoapp.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.edlo.mydemoapp.R
import com.edlo.mydemoapp.databinding.FragmentUserDetailBinding
import com.edlo.mydemoapp.ui.base.BaseFragment
import com.edlo.mydemoapp.util.GlideApp
import io.reactivex.disposables.Disposable


class GithubUserDetailFragment : BaseFragment<FragmentUserDetailBinding>() {

    private lateinit var viewModel: GithubUsersViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initViewBinding(inflater: LayoutInflater,
                                 container: ViewGroup?, savedInstanceState: Bundle?): FragmentUserDetailBinding {
        return FragmentUserDetailBinding.inflate(inflater, container, false)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity() as ViewModelStoreOwner).get(GithubUsersViewModel::class.java)
        initToolbar(viewModel.currentSelectedUser.value!!.login)
        viewModel.getUserDatails()
    }

    override fun addDisposable() {
        disposable.add(
            subscribeUserDetailUpdate()
        )
    }

    fun subscribeUserDetailUpdate(): Disposable {
        return viewModel.onUserDetailUpdate.subscribe { user ->
            initView()
        }
    }

    private fun initView() {
        viewModel.currentSelectedUser.value?.let { user ->
            binding.txtName.text = user.login
            binding.txtInfo1.text = getString(R.string.detailUserFollowers, user.followers)
            binding.txtInfo2.text = getString(R.string.detailUserFollowing, user.following)
            binding.txtInfo3.text = getString(R.string.detailUserPublicRepos, user.publicRepos)
            binding.txtInfo4.text = getString(R.string.detailUserPublicGists, user.publicGists)
            binding.btnVisit.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(user.htmlUrl))
                startActivity(intent)
            }

            GlideApp.with(binding.root.context)
                .load(user.avatarUrl)
                .placeholder(R.drawable.shape_thumb)
                .into(binding.imgUser)
        }
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.setCurrentSelectedUser(null)
    }
}