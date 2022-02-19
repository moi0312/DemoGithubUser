package com.edlo.mydemoapp.ui.base

import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppActivity() {

    var disposable = CompositeDisposable()

    abstract fun initViewModel()
    abstract fun addDisposable()

    override fun onStart() {
        super.onStart()
        addDisposable()
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
}