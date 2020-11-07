package com.yoji.likeshare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.yoji.likeshare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postViewModel = PostViewModel(PostRepositoryInMemoryImplementation())
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
            with(binding.newContentEdtTxtId){
                postViewModel.changeContent(text.toString())
                postViewModel.save()

                setText("")
                binding.prevContentTxtViewId.text = ""
                clearFocus()
                AndroidUtils.hideKeyboard(this)
                binding.motionLayoutId.transitionToStart()
            }
        }

        postViewModel.data.observe(this, { posts -> postAdapter.submitList(posts) })
    }
}