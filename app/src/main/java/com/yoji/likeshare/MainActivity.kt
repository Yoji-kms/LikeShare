package com.yoji.likeshare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.yoji.likeshare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val postViewModel by lazy { PostViewModel(PostRepositoryInMemoryImplementation()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val postAdapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                postViewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                postViewModel.shareById(post.id)
            }

            override fun onRemove(post: Post) {
                postViewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                binding.motionLayoutId.transitionToEnd()
                binding.prevContentTxtViewId.text = post.content
                postViewModel.edit(post)
            }
        })

        binding.postListViewId.adapter = postAdapter

        binding.newContentEdtTxtId.addTextChangedListener(
            onTextChanged = { s, _, _, _ -> binding.saveBtnId.isEnabled = !s.isNullOrBlank() }
        )

        binding.saveBtnId.setOnClickListener {
            with(binding.newContentEdtTxtId) {
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(this@MainActivity,
                        "Content is empty",
                        Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

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