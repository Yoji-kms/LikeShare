package com.yoji.likeshare.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yoji.likeshare.R
import com.yoji.likeshare.application.App
import com.yoji.likeshare.databinding.FragmentItemBinding
import com.yoji.likeshare.dto.Post
import com.yoji.likeshare.listeners.OnInteractionListener
import com.yoji.likeshare.repository.PostRepositoryJsonImplementation
import com.yoji.likeshare.viewmodel.PostViewModel

class ItemFragment : Fragment() {

    private val binding by lazy { FragmentItemBinding.inflate(layoutInflater) }
    private val postViewModel: PostViewModel by viewModels(ownerProducer = ::requireActivity)


    private val onInteractionListener = object : OnInteractionListener {
        override fun onLike(post: Post) {
            postViewModel.likeById(post.id)
        }

        override fun onShare(post: Post) {
            postViewModel.shareById(post.id)
        }

        override fun onRemove(post: Post) {
            findNavController().navigate(R.id.action_itemFragment_to_mainFragment)
            postViewModel.removeById(post.id)
        }

        override fun onEdit(post: Post) {
            findNavController().navigate(R.id.action_itemFragment_to_createOrEditFragment)
            postViewModel.edit(post)
        }

        override fun onPlayVideo(post: Post) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(post.video)))
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val post = postViewModel.selectedPost.value
        if (post == null) {
            Toast.makeText(context, "Error: Can not load post!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_itemFragment_to_mainFragment)
            return binding.root
        }
        binding.apply {
            toolbarId.title = post.author
            toolbarId.subtitle = post.published
            toolbarId.navigationIcon = with(App.applicationContext().resources) {
                getDrawable(post.avatar, null)
                    ?: getDrawable(PostRepositoryJsonImplementation.DEF_AVATAR_ID, null)
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

        postViewModel.data.observe(
            viewLifecycleOwner,
            { posts ->
                with(posts.singleOrNull { it.id == post.id }) {
                    binding.likesCheckBoxId.text = this?.likesCounter?.toFormattedString() ?: "0"
                    binding.shareBtnId.text = this?.shareCounter?.toFormattedString() ?: "0"
                }
            })

        return binding.root
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