package com.yoji.likeshare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.yoji.likeshare.callbacks.PostDiffCallback
import com.yoji.likeshare.viewholder.PostViewHolder
import com.yoji.likeshare.databinding.FragmentItemBinding
import com.yoji.likeshare.dto.Post
import com.yoji.likeshare.listeners.OnPostClickListener

class PostAdapter(
    private val onPostClickListener: OnPostClickListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = FragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(
            binding = binding,
            onPostClickListener = onPostClickListener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}