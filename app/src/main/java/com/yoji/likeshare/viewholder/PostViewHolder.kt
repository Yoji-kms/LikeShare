package com.yoji.likeshare.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yoji.likeshare.R
import com.yoji.likeshare.application.App
import com.yoji.likeshare.databinding.FragmentItemBinding
import com.yoji.likeshare.dto.Post
import com.yoji.likeshare.listeners.OnPostClickListener
import com.yoji.likeshare.repository.PostRepositoryJsonImplementation.Companion.context
import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.StringBuilder

class PostViewHolder(
    private val binding: FragmentItemBinding,
    private val onPostClickListener: OnPostClickListener
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("UseCompatLoadingForDrawables")
    fun bind(post: Post) {
        binding.apply {
            toolbarId.title = post.author
            toolbarId.subtitle = post.published.toFormattedString()
            toolbarId.navigationIcon = with(App.applicationContext().resources){
                getDrawable(post.avatar, null) ?:
                getDrawable(Post.DEF_AVATAR_ID, null)
            }
            if (!post.video.isNullOrBlank()) videoView
                .also { it.visibility = View.VISIBLE }
            else videoView.visibility = View.GONE
            if (post.content.isNotBlank()) textTxtViewId
                .also { it.visibility = View.VISIBLE }
                .text = post.content
            else textTxtViewId.visibility = View.GONE
            likesCheckBoxId.isClickable = false
            likesCheckBoxId.isChecked = post.likedByMe
            likesCheckBoxId.text = post.likesCounter.toFormattedString()
            shareBtnId.text = post.shareCounter.toFormattedString()
            watchesBtnId.text = post.watchesCounter.toFormattedString()

            toolbarId.setOnClickListener { onPostClickListener.onClick(post) }
            bottomPanel.setOnClickListener { onPostClickListener.onClick(post) }
            textTxtViewId.setOnClickListener { onPostClickListener.onClick(post) }
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

    @SuppressLint("SimpleDateFormat")
    private fun Date.toFormattedString() = StringBuilder()
        .append(SimpleDateFormat("dd MMM yyyy").format(this))
        .append(context.getString(R.string.published_at))
        .append(SimpleDateFormat("hh:mm:ss").format(this))
        .toString()
}