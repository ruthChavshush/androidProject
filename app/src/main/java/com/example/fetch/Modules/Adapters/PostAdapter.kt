package com.example.fetch.Modules.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fetch.Models.Post
import com.example.fetch.Models.PostTypes
import com.example.fetch.Modules.Profile.ProfileFragmentDirections
import com.example.fetch.databinding.ItemPostBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PostAdapter(
    private val navController: NavController?,
    private val isEdit: Boolean,
    private val callback: PostAdapterCallback?
) :
    ListAdapter<Post, PostAdapter.PostViewHolder>(PostViewHolder.PostDiffCallback()) {

    interface PostAdapterCallback {
        fun onPostDeleted(post: Post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, navController, isEdit, callback)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    class PostViewHolder(
        private val binding: ItemPostBinding,
        private val navController: NavController?,
        private val isEdit: Boolean,
        private val callback: PostAdapterCallback?
    ) : RecyclerView.ViewHolder(binding.root) {
        private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

        fun bind(post: Post) {
            binding.tvUserName.text = post.petName
            binding.tvCaption.text = post.caption
            binding.tvLocation.text = post.location
            binding.tvPetName.text = post.petName
            binding.tvTimestamp.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(
                Date(post.timestamp)
            )

            if (post.postType == PostTypes.PLAYDATE) {
                binding.tvPlaydateWith.visibility = View.VISIBLE
                binding.tvPetName.text = post.petName // Set pet name as usual

                binding.btnJoin.visibility = View.VISIBLE
            } else {
                binding.tvPlaydateWith.visibility = View.GONE
                binding.btnJoin.visibility = View.GONE
            }

            if (!isEdit) {
                binding.btnEditPost.visibility = View.GONE
                binding.btnDeletePost.visibility = View.GONE
            }

            binding.btnJoin.setOnClickListener {
                Toast.makeText(binding.root.context, "Joined Playdate!", Toast.LENGTH_SHORT).show()
            }

            // Load image using an image loading library like Glide or Picasso
            Picasso.get().load(post.imageUrl).into(binding.ivImage)

            // Set the click listener for the edit button
            binding.btnEditPost.setOnClickListener {
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToAddPostFragment(
                        null,
                        post
                    )
                navController?.navigate(action)
            }

            binding.btnDeletePost.setOnClickListener {
                deletePost(post)
            }

            getDisplayNameByUserId(post.userId) { displayName ->
                if (displayName != null) {
                    binding.tvUserName.text = displayName
                } else {
                    binding.tvUserName.text = "Unknown User"
                }
            }
        }

        private fun deletePost(post: Post) {
            firestore.collection("posts").document(post.postId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(binding.root.context, "Post deleted", Toast.LENGTH_SHORT).show()
                    // Notify the parent fragment about the deletion
                    callback?.onPostDeleted(post)
                }
                .addOnFailureListener { e ->
                    Log.w("PostAdapter", "Error deleting post", e)
                    Toast.makeText(
                        binding.root.context,
                        "Failed to delete post",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        private fun getDisplayNameByUserId(userId: String, callback: (String?) -> Unit) {
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        callback(document.getString("displayName"))
                    } else {
                        callback(null)
                    }
                }
                .addOnFailureListener { exception ->
                    callback(null)
                }
        }

        class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.timestamp == newItem.timestamp
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }
        }
    }
}
