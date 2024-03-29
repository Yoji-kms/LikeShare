package com.yoji.likeshare.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yoji.likeshare.R
import com.yoji.likeshare.adapter.PostAdapter
import com.yoji.likeshare.databinding.FragmentMainBinding
import com.yoji.likeshare.dto.Post
import com.yoji.likeshare.listeners.OnPostClickListener
import com.yoji.likeshare.viewmodel.PostViewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val postViewModel : PostViewModel by viewModels(ownerProducer = ::requireActivity)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val postAdapter = PostAdapter(object : OnPostClickListener {
            override fun onClick(post: Post) {
                postViewModel.edit(post)
                findNavController().navigate(R.id.action_mainFragment_to_itemFragment)
            }
        })

        binding.postListViewId.adapter = postAdapter

        binding.createPostFab.setOnClickListener {
            postViewModel.clear()
            findNavController().navigate(R.id.action_mainFragment_to_createOrEditFragment)
        }

        postViewModel.data.observe(viewLifecycleOwner) { posts -> postAdapter.submitList(posts) }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}