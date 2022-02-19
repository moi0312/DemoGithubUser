package com.edlo.mydemoapp.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class EmptyViewSupportAdapter<VH: RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    lateinit var recyclerView: RecyclerView
    var emptyView: View? = null
    var emptyObserver: RecyclerView.AdapterDataObserver = object: RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            emptyView?.let { view ->
                if (itemCount == 0) {
                    view.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    view.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
        }
    }

    init {
        registerAdapterDataObserver(emptyObserver)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }
}