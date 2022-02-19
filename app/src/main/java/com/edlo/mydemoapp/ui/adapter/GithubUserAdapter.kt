package com.edlo.mydemoapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.edlo.mydemoapp.R
import com.edlo.mydemoapp.databinding.ItemGithubUserBinding
import com.edlo.mydemoapp.repository.net.github.data.GithubUserData
import com.edlo.mydemoapp.util.GlideApp


class GithubUserAdapter: EmptyViewSupportAdapter<GithubUserAdapter.GithubUserViewHolder>() {

    var data = ArrayList<GithubUserData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onClick: ((Int, GithubUserData) -> Unit)? = null

    override fun getItemCount(): Int { return data.size }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserViewHolder {
        return  GithubUserViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int) {
        var itemData = data[position]
        holder.bind(itemData)
        onClick?.let {
            holder.itemView.setOnClickListener {
             it(position, itemData)
            }
        }
    }

//    fun removeItem(adapterPosition: Int) {
//        notifyItemRemoved(adapterPosition)
//        data.removeAt(adapterPosition)
//    }

    class GithubUserViewHolder(private val binding: ItemGithubUserBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun create(parent: ViewGroup): GithubUserViewHolder {
                var binding = ItemGithubUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return GithubUserViewHolder(binding)
            }

            var factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
        }

        private lateinit var item: GithubUserData

        fun bind(item: GithubUserData) {
            this.item = item
            binding.txtName.text = item.login

            GlideApp.with(binding.root.context)
                .load(item.avatarUrl)
                .transition(withCrossFade(factory))
                .placeholder(R.drawable.shape_thumb)
                .into(binding.imgUser)
        }

    }
}