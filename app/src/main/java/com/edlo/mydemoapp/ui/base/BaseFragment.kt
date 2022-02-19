package com.edlo.mydemoapp.ui.base

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.edlo.mydemoapp.R
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<VDB: ViewBinding> : AppFragment() {

    lateinit var binding: VDB

    var disposable = CompositeDisposable()

    abstract fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?,
                                 savedInstanceState: Bundle?): VDB
    abstract fun initViewModel()
    abstract fun addDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                    savedInstanceState: Bundle?): View {
        binding = initViewBinding(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    fun initToolbar(title: String, toolbarId: Int = R.id.toolbar): Toolbar? {
        binding.root.findViewById<Toolbar>(toolbarId)?.let {
            if(requireActivity().supportFragmentManager.backStackEntryCount > 0) {
                it.navigationIcon = requireActivity().getDrawable(R.drawable.ic_arrow_back)
                it.contentInsetStartWithNavigation = 0
                it.setNavigationOnClickListener {
                    requireActivity().onBackPressed()
                }
            }
            it.setTitleTextColor(Color.WHITE)
            it.title = title
            return it
        } ?: kotlin.run {
            return null
        }
    }

    override fun onStart() {
        super.onStart()
        addDisposable()
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
}