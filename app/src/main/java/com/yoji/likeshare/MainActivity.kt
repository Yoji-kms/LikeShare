package com.yoji.likeshare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.yoji.likeshare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val postViewModel by lazy { PostViewModel(PostRepositoryInMemoryImplementation()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val postAdapter = PostAdapter(
            onLikeListener = { post -> postViewModel.likeById(post.id) },
            onShareListener = { post -> postViewModel.shareById(post.id) },
            onRemoveListener = { post -> postViewModel.removeById(post.id) },
            onEditListener = { post ->
                binding.motionLayoutId.transitionToEnd()
                binding.prevContentTxtViewId.text = post.content
                postViewModel.edit(post)
            }
        )
        binding.postListViewId.adapter = postAdapter

        binding.newContentEdtTxtId.addTextChangedListener(
            onTextChanged = { s, _, _, _ -> binding.saveBtnId.isEnabled = !s.isNullOrBlank() }
        )

        binding.saveBtnId.setOnClickListener {
            with(binding.newContentEdtTxtId) {
                postViewModel.changeContent(text.toString())
                postViewModel.save()

                hideEditContentPanel()
            }
        }

        binding.cancelBtnId.setOnClickListener {
            postViewModel.clear()
            hideEditContentPanel()
        }

        postViewModel.data.observe(this, { posts -> postAdapter.submitList(posts) })
    }

    private fun hideEditContentPanel() {
        AndroidUtils.hideKeyboard(binding.newContentEdtTxtId)
        postViewModel.clear()
        binding.motionLayoutId.transitionToStart()
    }
}