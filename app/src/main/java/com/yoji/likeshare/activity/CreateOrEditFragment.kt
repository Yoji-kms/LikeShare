package com.yoji.likeshare.activity

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yoji.likeshare.R
import com.yoji.likeshare.databinding.FragmentCreateOrEditBinding
import com.yoji.likeshare.viewmodel.PostViewModel

class CreateOrEditFragment : Fragment() {

    private val binding by lazy { FragmentCreateOrEditBinding.inflate(layoutInflater) }
    private val postViewModel : PostViewModel by viewModels (ownerProducer = ::requireActivity)
    private val lines by lazy { binding.prevContentTxtView.lineCount }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.prevContentTxtView.also { it.post { kotlin.run { lines } } }.maxLines = 1

        val previousContent = postViewModel.selectedPost.value?.content

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
            postViewModel.changeContent(binding.newContentEdtTxt.text.toString())
            postViewModel.save()
            findNavController().navigate(R.id.action_createOrEditFragment_to_mainFragment)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        postViewModel.clear()
    }
}