package com.yoji.likeshare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.yoji.likeshare.listeners.OnInteractionListener
import com.yoji.likeshare.callbacks.PostDiffCallback
import com.yoji.likeshare.viewholder.PostViewHolder
import com.yoji.likeshare.databinding.ItemPostBinding
import com.yoji.likeshare.dto.Post

class PostAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(
            binding = binding,
            onInteractionListener = onInteractionListener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}