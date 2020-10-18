package com.yoji.likeshare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.yoji.likeshare.databinding.ActivityMainBinding
import java.lang.NumberFormatException
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var likesCounter = 0
    private var shareCounter = 0
    private var watchesCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
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

    private fun Int.roundToThousandsWithOneDecimal(): Double =
            (this / 100).toDouble() / 10

    private fun init() {
        initCheckBox()
        shareCounter = initTxtView(binding.shareCounterTxtViewId)
        watchesCounter = initTxtView(binding.watchesCounterTxtViewId)
        initBtns()
    }

    private fun initTxtView(txtView: TextView): Int {
        txtView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    if (txtView.text.toString().toInt().toFormattedString() != s.toString())
                        txtView.text = s.toString().toInt().toFormattedString()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        val result = Random.nextInt(from = 0, until = 999)
        txtView.text = result.toString()
        return result
    }

    private fun initCheckBox() {
        binding.likesCheckBoxId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    if (binding.likesCheckBoxId.text.toString().toInt().toFormattedString() !=
                            s.toString()
                    )
                        binding.likesCheckBoxId.text = s.toString().toInt().toFormattedString()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.likesCheckBoxId.setOnCheckedChangeListener { buttonView, isChecked ->
            buttonView.text = if (isChecked) {
                likesCounter++
                (likesCounter).toFormattedString()
            } else {
                likesCounter--
                (likesCounter).toFormattedString()
            }
        }

        likesCounter = Random.nextInt(from = 0, until = 999)

        binding.likesCheckBoxId.text = likesCounter.toString()
    }

    private fun initBtns() {
        binding.resetCountersBtnId.setOnClickListener {
            likesCounter = Random.nextInt(from = 0, until = Int.MAX_VALUE)
            watchesCounter = Random.nextInt(from = 0, until = Int.MAX_VALUE)
            shareCounter = Random.nextInt(from = 0, until = Int.MAX_VALUE)
            binding.likesCheckBoxId.text = likesCounter.toString()
            binding.shareCounterTxtViewId.text = shareCounter.toString()
            binding.watchesCounterTxtViewId.text = watchesCounter.toString()
        }

        binding.shareImgBtnId.setOnClickListener {
            shareCounter++
            binding.shareCounterTxtViewId.text =
                    (shareCounter).toFormattedString()
        }
    }
}