package com.yoji.likeshare

import androidx.recyclerview.widget.RecyclerView
import com.yoji.likeshare.databinding.ItemPostBinding

class PostViewHolder
    (
    private val binding: ItemPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            toolbarId.title = post.author
            toolbarId.subtitle = post.published
            toolbarId.navigationIcon = post.avatar
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
            textTxtViewId.text = post.content
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