package com.edlo.mydemoapp.ui.base

import android.os.Bundle
import android.view.WindowManager
import io.reactivex.disposables.CompositeDisposable
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

abstract class BaseActivity : AppActivity() {

    var disposable = CompositeDisposable()

    abstract fun initViewModel()
    abstract fun addDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.hide()
        addDisposable()
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
}