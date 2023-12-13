package com.app.newz.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.newz.databinding.ItemPostBinding
import com.app.newz.db.PostEntity
import com.app.newz.ui.PostListFragmentDirections

class PostAdapter(private val navController: NavController) : RecyclerView.Adapter<PostAdapter.ViewHolder>(){
    private lateinit var binding: ItemPostBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemPostBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: PostEntity) {
            //InitView
            binding.apply {
                itemPostTitle.text = item.postTitle

                // Truncate the text to 80 characters and add "..." if it's longer
                val content = item.postContent
                if (content.length > 80) {
                    itemPostContent.text = content.substring(0, 60) + "..."
                } else {
                    itemPostContent.text = content
                }

                binding.root.setOnClickListener {
                    val action = PostListFragmentDirections.actionPostListFragmentToViewPostFragment(item.postId, "", "")
                    navController.navigate(action)
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<PostEntity>() {
        override fun areItemsTheSame(oldItem: PostEntity, newItem: PostEntity): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(oldItem: PostEntity, newItem: PostEntity): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}