package com.edlo.mydemoapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import com.edlo.mydemoapp.databinding.FragmentListBinding
import com.edlo.mydemoapp.ui.adapter.GithubUserAdapter
import com.edlo.mydemoapp.ui.base.BaseFragment
import kotlinx.coroutines.launch

class GithubUsersFragment : BaseFragment<FragmentListBinding>() {

    private lateinit var viewModel: GithubUsersViewModel
    private var adapter = GithubUserAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        lifecycleScope.launch {
//            viewModel.getGitHubUsers().collectLatest { adapter.submitData(it) }
        }

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

    override fun addDisposable() { }
}