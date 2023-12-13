package com.app.newz.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.app.newz.R
import com.app.newz.databinding.FragmentAddPostBinding
import com.app.newz.db.PostDatabase
import com.app.newz.db.PostEntity
import com.app.newz.utils.Constants.POST_DB
import com.google.android.material.snackbar.Snackbar


class AddPostFragment : Fragment() {

    private var _binding: FragmentAddPostBinding? = null
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
    private lateinit var postEntity: PostEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddPostBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            // cancel creating new post
            btnCancelPost.setOnClickListener {
                val action = AddPostFragmentDirections.actionAddPostFragmentToPostListFragment("", "")
                findNavController().navigate(action)
            }

            // save new post
            btnSavePost.setOnClickListener {
                val title = edtTitle.text.toString()
                val content = edtContent.text.toString()
                val snackBarLayout = snackBarLayout

                if (title.isNotEmpty() and content.isNotEmpty()) {
                    postEntity = PostEntity(0, title, content)
                    postDatabase.dao().insertPost(postEntity)

                    val action = AddPostFragmentDirections.actionAddPostFragmentToPostListFragment("", "Post saved")
                    findNavController().navigate(action)
                } else {
                    Snackbar.make(snackBarLayout, "Post title and content cannot be empty.", Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.red))
                        .setTextColor(resources.getColor(R.color.white))
                        .show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}