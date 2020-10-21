package com.yoji.likeshare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.yoji.likeshare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postViewModel = PostViewModel(PostRepositoryInMemoryImplementation())
        postViewModel.data.observe(this, { post ->
            with(binding) {
                postCardViewId.toolbarId.title = post.author
                postCardViewId.toolbarId.subtitle = post.published
                postCardViewId.textTxtViewId.text = post.content
                postCardViewId.likesCheckBoxId.isChecked = post.likedByMe
                postCardViewId.likesCheckBoxId.text = post.likesCounter.toFormattedString()
                postCardViewId.shareCounterTxtViewId.text = post.shareCounter.toFormattedString()
                postCardViewId.watchesCounterTxtViewId.text =
                    post.watchesCounter.toFormattedString()
            }
        }
        )

        binding.postCardViewId.likesCheckBoxId.setOnCheckedChangeListener { _, _ ->
            postViewModel.like()
        }

        binding.resetCountersBtnId.setOnClickListener {
            postViewModel.randomCounters()
        }

        binding.postCardViewId.shareImgBtnId.setOnClickListener {
            postViewModel.share()
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