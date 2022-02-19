package com.edlo.mydemoapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
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

    override fun onStart() {
        super.onStart()
        addDisposable()
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
}