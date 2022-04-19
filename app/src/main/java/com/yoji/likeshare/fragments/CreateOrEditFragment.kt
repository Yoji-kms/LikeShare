package com.yoji.likeshare.fragments

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yoji.likeshare.R
import com.yoji.likeshare.application.App
import com.yoji.likeshare.databinding.FragmentCreateOrEditBinding
import com.yoji.likeshare.utils.AndroidUtils
import com.yoji.likeshare.viewmodel.PostViewModel

class CreateOrEditFragment : Fragment() {

    private var _binding: FragmentCreateOrEditBinding? = null
    private val binding get() = _binding!!
    private val postViewModel: PostViewModel by viewModels(ownerProducer = ::requireActivity)
    private val lines by lazy { binding.prevContentTxtView.lineCount }
    private val previousContent by lazy { postViewModel.selectedPost.value?.content }
    private val prefs = App.applicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
    private val key = "saved content"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateOrEditBinding.inflate(inflater, container, false)
        binding.prevContentTxtView.also { it.post { kotlin.run { lines } } }.maxLines = 1

        binding.apply {
            if (previousContent.isNullOrBlank()) {
                prevContentTxtView.visibility = View.GONE
                copyToEdtTxtBtn.visibility = View.GONE
                showMoreBtn.visibility = View.GONE
                with(prefs.getString(key, "")){
                    newContentEdtTxt.setText(this)
                    if (!this.isNullOrBlank()) {
                        clearEdtTxtBtn.isEnabled = true
                        saveBtnId.isEnabled = true
                    }
                }
            } else {
                prevContentTxtView.visibility = View.VISIBLE
                copyToEdtTxtBtn.visibility = View.VISIBLE
                showMoreBtn.visibility = View.VISIBLE
                prevContentTxtView.text = previousContent
            }
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
            binding.constraintLayout.invalidate()
            binding.nestedScrlViewId.invalidate()
            binding.nestedScrlViewId.requestLayout()
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
            postViewModel.changeContent(binding.newContentEdtTxt.text.toString())
            postViewModel.save()
            with(prefs.edit()){
                clear()
                apply()
            }
            it.clearFocus()
            AndroidUtils.hideKeyboard(it)
            findNavController().navigate(R.id.action_createOrEditFragment_to_mainFragment)
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (previousContent.isNullOrBlank()) {
                        with(
                            prefs.edit()
                        ) {
                            putString(key, binding.newContentEdtTxt.text.toString().trim())
                            apply()
                        }
                        findNavController().navigate(R.id.action_createOrEditFragment_to_mainFragment)
                    } else {
                        findNavController().navigate(R.id.action_createOrEditFragment_to_itemFragment)
                    }
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}