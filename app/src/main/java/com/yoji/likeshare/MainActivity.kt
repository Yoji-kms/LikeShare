package com.yoji.likeshare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yoji.likeshare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postViewModel = PostViewModel(PostRepositoryInMemoryImplementation())
        val postAdapter = PostAdapter(
            onLikeListener = { post -> postViewModel.likeById(post.id) },
            onShareListener = { post -> postViewModel.shareById(post.id) }
        )
        binding.postListViewId.adapter = postAdapter

        postViewModel.data.observe(this, { posts -> postAdapter.submitList(posts) })

        binding.resetCountersBtnId.setOnClickListener { postViewModel.randomCounters() }
    }
}