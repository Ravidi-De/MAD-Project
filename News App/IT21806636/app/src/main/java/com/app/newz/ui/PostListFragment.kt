package com.app.newz.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.app.newz.R
import com.app.newz.adapter.PostAdapter
import com.app.newz.databinding.FragmentPostListBinding
import com.app.newz.db.PostDatabase
import com.app.newz.utils.Constants.POST_DB
import com.google.android.material.snackbar.Snackbar


class PostListFragment : Fragment() {

    private var _binding: FragmentPostListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    // database conn
    private val postDatabase : PostDatabase by lazy {
        Room.databaseBuilder(requireContext(), PostDatabase::class.java, POST_DB)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    private val postAdapter by lazy { PostAdapter(findNavController()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPostListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val errorMessage = requireArguments().getString("errorMessage", "")
        if (errorMessage.isNotEmpty()) {
            Snackbar.make(binding.snackBarLayout, errorMessage, Snackbar.LENGTH_LONG)
                .setBackgroundTint(resources.getColor(R.color.red))
                .setTextColor(resources.getColor(R.color.white))
                .show()
        }

        val successMessage = requireArguments().getString("successMessage", "")
        if (successMessage.isNotEmpty()) {
            Snackbar.make(binding.snackBarLayout, successMessage, Snackbar.LENGTH_LONG)
                .setBackgroundTint(resources.getColor(R.color.green))
                .setTextColor(resources.getColor(R.color.white))
                .show()
        }
        arguments?.clear()

        binding.btnAddPost.setOnClickListener {
            findNavController().navigate(R.id.action_PostListFragment_to_AddPostFragment)
        }
        checkPostItem()
    }

    private fun checkPostItem() {
        binding.apply {
            if (postDatabase.dao().getAllPosts().isNotEmpty()) {
                postList.visibility = View.VISIBLE
                postListEmptyText.visibility = View.GONE
                postAdapter.differ.submitList(postDatabase.dao().getAllPosts())
                setupRecyclerView()
            } else {
                postList.visibility = View.GONE
                postListEmptyText.visibility = View.VISIBLE
            }
        }
    }

    private fun setupRecyclerView() {
        binding.postList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter=postAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}