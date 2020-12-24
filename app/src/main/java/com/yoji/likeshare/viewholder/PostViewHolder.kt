package com.yoji.likeshare.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yoji.likeshare.listeners.OnInteractionListener
import com.yoji.likeshare.R
import com.yoji.likeshare.application.App
import com.yoji.likeshare.databinding.ItemPostBinding
import com.yoji.likeshare.dto.Post
import com.yoji.likeshare.repository.PostRepositoryJsonImplementation

class PostViewHolder
    (
    private val binding: ItemPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("UseCompatLoadingForDrawables")
    fun bind(post: Post) {
        binding.apply {
            toolbarId.title = post.author
            toolbarId.subtitle = post.published
            toolbarId.navigationIcon = with(App.applicationContext().resources){
                getDrawable(post.avatar, null) ?:
                getDrawable(PostRepositoryJsonImplementation.DEF_AVATAR_ID, null)
            }

            toolbarId.also { it.menu.clear() }.inflateMenu(R.menu.toolbar_menu)
            toolbarId.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.delete_post -> {
                        onInteractionListener.onRemove(post)
                        true
                    }
                    R.id.edit_post -> {
                        onInteractionListener.onEdit(post)
                        true
                    }
                    else -> false
                }
            }
            if (!post.video.isNullOrBlank()) videoView
                .also { it.visibility = View.VISIBLE }
                .setOnClickListener { onInteractionListener.onPlayVideo(post) }
            else videoView.visibility = View.GONE
            if (post.content.isNotBlank()) textTxtViewId
                .also { it.visibility = View.VISIBLE }
                .text = post.content
            else textTxtViewId.visibility = View.GONE
            likesCheckBoxId.isChecked = post.likedByMe
            likesCheckBoxId.text = post.likesCounter.toFormattedString()
            likesCheckBoxId.setOnClickListener { onInteractionListener.onLike(post) }
            shareBtnId.text = post.shareCounter.toFormattedString()
            shareBtnId.setOnClickListener { onInteractionListener.onShare(post) }
            watchesBtnId.text = post.watchesCounter.toFormattedString()
        }
    }

    private fun Int.toFormattedString() = when (this) {
        in 0..999 -> this.toString()
        in 1_000..9_999 -> this.roundToThousandsWithOneDecimal().toString() + "K"
        in 10_000..999_999 -> (this / 1_000).toString() + "K"
        in 1_000_000..9_999_999 -> (this / 1_000).roundToThousandsWithOneDecimal().toString() + "M"
        in 10_000_000..999_999_999 -> (this / 1_000_000).toString() + "M"
        in 1_000_000_000..Int.MAX_VALUE -> (this / 1_000_000).roundToThousandsWithOneDecimal()
            .toString() + "B"
        else -> "0"
    }

    private fun Int.roundToThousandsWithOneDecimal(): Double = (this / 100).toDouble() / 10
}