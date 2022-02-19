package com.edlo.mydemoapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import com.edlo.mydemoapp.R
import com.edlo.mydemoapp.databinding.FragmentListBinding
import com.edlo.mydemoapp.ui.adapter.GithubUserAdapter
import com.edlo.mydemoapp.ui.base.BaseFragment
import com.edlo.mydemoapp.ui.base.BaseViewModel
import com.edlo.mydemoapp.util.Consts.Companion.OVER_SCROLL_OFFSET
import kotlinx.coroutines.launch
import me.everything.android.ui.overscroll.IOverScrollState
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

class GithubUsersFragment : BaseFragment<FragmentListBinding>() {

    private lateinit var viewModel: GithubUsersViewModel
    private var adapter = GithubUserAdapter()
    private var isOverScroll = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(getString(R.string.app_name))
        initView()
        initViewModelObserve()
    }

    override fun onResume() {
        super.onResume()
        if(viewModel.getSearchKey().value.isNullOrEmpty())
            viewModel.onSearchUser.onNext("a")
    }

    private fun initView() {
        binding.listView.layoutManager = GridLayoutManager(activity, 3)
        binding.listView.adapter = adapter
        adapter.emptyView = binding.txtListEmpty
        adapter.onClick = { index, data ->
            viewModel.setCurrentSelectedUser(data)
            viewModel.onSelectedUser.onNext(data)
        }

        var decor = OverScrollDecoratorHelper.setUpOverScroll(
            binding.listView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL)
        decor.setOverScrollUpdateListener{ decor, state, offset ->
//            Log.e(msg = "overScroll: $offset")
            isOverScroll = offset < -OVER_SCROLL_OFFSET
        }
        decor.setOverScrollStateListener { decor, oldState, newState ->
            if (isOverScroll && newState == IOverScrollState.STATE_BOUNCE_BACK) {
                when(oldState) {
//                    IOverScrollState.STATE_DRAG_START_SIDE -> viewModel.onScrollReachesEdge.onNext(BaseViewModel.SCROLL_OVER_TOP)
                    IOverScrollState.STATE_DRAG_END_SIDE -> {
                        viewModel.onScrollReachesEdge.onNext(BaseViewModel.SCROLL_OVER_BOTTOM)
                    }
                    else -> {}
                }
            }
        }


        binding.imgClearSearch.setOnClickListener {
            viewModel.listGitHubUsers("")
        }
        binding.inputSearch.setOnEditorActionListener { view, actionId, _ ->
            when(actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    viewModel.onSearchUser.onNext(binding.inputSearch.text.toString())
                    false
                }
                else -> false
            }
        }
    }

    private fun initViewModelObserve() {

        viewModel.getGitHubUsers().observe(requireActivity() as LifecycleOwner, Observer {
            adapter.data = it
        })
        viewModel.getSearchKey().observe(activity as LifecycleOwner, Observer { key ->
            binding.inputSearch.setText(key)
            if(key.isNotEmpty()){
                binding.imgClearSearch.visibility = View.VISIBLE
            } else {
                binding.imgClearSearch.visibility = View.GONE
            }
        })

    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity() as ViewModelStoreOwner).get(GithubUsersViewModel::class.java)
    }

    override fun initViewBinding(inflater: LayoutInflater,
            container: ViewGroup?, savedInstanceState: Bundle?): FragmentListBinding {
        return FragmentListBinding.inflate(inflater, container, false)
    }

    override fun addDisposable() {
        disposable.add(
            viewModel.onScrollReachesEdge.subscribe {
                when(it) {
//                    BaseViewModel.SCROLL_OVER_TOP -> { }
                    BaseViewModel.SCROLL_OVER_BOTTOM -> {
                        viewModel.getMoreUsers()
                    }
                    else -> { }
                }
            }
        )

    }
}