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
import com.app.newz.databinding.FragmentUpdatePostBinding
import com.app.newz.db.PostDatabase
import com.app.newz.db.PostEntity
import com.app.newz.utils.Constants


class UpdatePostFragment : Fragment() {

    private var _binding: FragmentUpdatePostBinding? = null
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
        _binding = FragmentUpdatePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postId = requireArguments().getInt("postId", -1)
        if (postId == -1) {
            val action = UpdatePostFragmentDirections.actionUpdatePostFragmentToPostListFragment("Something went wrong!", "")
            findNavController().navigate(action)
        }
        arguments?.clear()

        val post = postDatabase.dao().getPost(postId)

        val postTitle = post.postTitle
        val postContent = post.postContent

        val postTitleView = view.findViewById<TextView>(R.id.postTitleEditView)
        val postContentView = view.findViewById<TextView>(R.id.postContentEditView)
        postTitleView.text = postTitle.toString()
        postContentView.text = postContent.toString()

        binding.apply {
            // cancel updating post
            btnCancelPost.setOnClickListener {
                val action = UpdatePostFragmentDirections.actionUpdatePostFragmentToPostListFragment("", "")
                findNavController().navigate(action)
            }

            // save updated post
            btnSavePost.setOnClickListener {
                val updatedTitle = postTitleEditView.text.toString()
                val updatedContent = postContentEditView.text.toString()

                postEntity = PostEntity(postId, updatedTitle, updatedContent)
                postDatabase.dao().updatePost(postEntity)

                val action = UpdatePostFragmentDirections.actionUpdatePostFragmentToPostListFragment("", "Post updated!")
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}