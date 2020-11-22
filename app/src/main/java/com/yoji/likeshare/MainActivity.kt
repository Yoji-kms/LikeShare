package com.yoji.likeshare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.yoji.likeshare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val postViewModel by lazy { PostViewModel(PostRepositoryInMemoryImplementation()) }

   companion object Code {
       const val newContent = "New content"
       const val prevContent = "Prev content"
   }


    val startCreateOrEditPostActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        when (result.resultCode){
            RESULT_OK -> {
                result.data?.getStringExtra(newContent)?.let { postViewModel.changeContent(it) }
                postViewModel.save()
            }
            RESULT_CANCELED -> postViewModel.clear()
        }
    }

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
                startCreateOrEditPostActivityForResult.launch(
                    Intent(this@MainActivity, CreateOrEditActivity::class.java)
                    .putExtra(prevContent, post.content))
                postViewModel.edit(post)
            }
        })

        binding.postListViewId.adapter = postAdapter

        binding.createPostFab.setOnClickListener {
            startCreateOrEditPostActivityForResult.launch(
                Intent(this, CreateOrEditActivity::class.java)
            )
        }

        postViewModel.data.observe(this, { posts -> postAdapter.submitList(posts) })
    }
}