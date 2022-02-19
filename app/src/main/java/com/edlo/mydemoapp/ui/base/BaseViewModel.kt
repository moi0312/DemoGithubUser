package com.edlo.mydemoapp.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.subjects.PublishSubject

open class BaseViewModel: ViewModel() {
    companion object {
        var SCROLL_OVER_TOP = 1
        var SCROLL_OVER_BOTTOM = 2
    }

    val onScrollReachesEdge: PublishSubject<Int> = PublishSubject.create()
    val onLoading: PublishSubject<Boolean> = PublishSubject.create()
}