package com.app.newz.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.app.newz.R
import com.app.newz.databinding.FragmentViewPostBinding
import com.app.newz.db.PostDatabase
import com.app.newz.db.PostEntity
import com.app.newz.utils.Constants
import com.google.android.material.snackbar.Snackbar


class ViewPostFragment : Fragment() {

    private var _binding: FragmentViewPostBinding? = null
    private val binding get() = _binding!!
    // database conn
    private val postDatabase : PostDatabase by lazy {
        Room.databaseBuilder(requireContext(), PostDatabase::class.java, Constants.POST_DB)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    private lateinit var postEntity: PostEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewPostBinding.inflate(inflater, container, false)
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

        val postId = requireArguments().getInt("postId", -1)
        if (postId == -1) {
            val action = ViewPostFragmentDirections.actionViewPostFragmentToPostListFragment("Something went wrong!", "")
            findNavController().navigate(action)
        }
        arguments?.clear()

        val post = postDatabase.dao().getPost(postId)

        val postTitle = post.postTitle
        val postContent = post.postContent

        val postTitleView = view.findViewById<TextView>(R.id.postTitleTextView)
        val postContentView = view.findViewById<TextView>(R.id.postContentTextView)
        postTitleView.text = postTitle.toString()
        postContentView.text = postContent.toString()

        binding.apply {
            // edit post
            btnEditPost.setOnClickListener {
                val action = ViewPostFragmentDirections.actionViewPostFragmentToUpdatePostFragment(postId)
                findNavController().navigate(action)
            }

            // delete post
            btnDeletePost.setOnClickListener {
                postEntity = PostEntity(postId, postTitle, postContent)
                postDatabase.dao().deletePost(postEntity)

                val action = ViewPostFragmentDirections.actionViewPostFragmentToPostListFragment("Post deleted!", "")
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}