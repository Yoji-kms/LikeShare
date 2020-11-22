package com.yoji.likeshare

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.yoji.likeshare.MainActivity.Code.newContent
import com.yoji.likeshare.MainActivity.Code.prevContent
import com.yoji.likeshare.databinding.ActivityCreateOrEditBinding

class CreateOrEditActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCreateOrEditBinding.inflate(layoutInflater) }
    private val lines by lazy { binding.prevContentTxtView.lineCount }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.prevContentTxtView.also { it.post { kotlin.run { lines } } }.maxLines = 1

        val previousContent = intent.getStringExtra(prevContent)

        if (previousContent.isNullOrBlank()){
            binding.editGroup.visibility = View.GONE
        }else {
            binding.editGroup.visibility = View.VISIBLE
            binding.prevContentTxtView.text = previousContent
        }

        binding.showMoreBtn.addOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                ObjectAnimator
                    .ofInt(binding.prevContentTxtView, "maxLines", lines).apply {
                        duration = 300
                        start()
                    }
            } else {
                ObjectAnimator
                    .ofInt(binding.prevContentTxtView, "maxLines", 1).apply {
                        duration = 300
                        start()
                    }
            }
        }

        binding.copyToEdtTxtBtn.setOnClickListener {
            binding.newContentEdtTxt.setText(binding.prevContentTxtView.text)
        }

        binding.newContentEdtTxt.addTextChangedListener(
            onTextChanged = { s, _, _, _ ->
                binding.clearEdtTxtBtn.isEnabled = !s.isNullOrBlank()
                binding.saveBtnId.isEnabled = !s.isNullOrBlank()
            }
        )

        binding.clearEdtTxtBtn.setOnClickListener {
            binding.newContentEdtTxt.setText("")
        }

        binding.saveBtnId.setOnClickListener {
            setResult(
                RESULT_OK,
                Intent().putExtra(newContent, binding.newContentEdtTxt.text.toString())
            )
            finish()
        }
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        finish()
    }
}