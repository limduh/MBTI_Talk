package com.example.mbti_talk.post

import android.content.Intent
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mbti_talk.R
import com.example.mbti_talk.databinding.ActivityPostItemBinding
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage


class PostAdapter(private var postList: List<PostData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // onCreateViewHolder 메서드에서 뷰홀더를 생성합니다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ActivityPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    // onBindViewHolder 메서드에서 뷰홀더에 데이터를 바인딩합니다.
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = postList[position]
        val viewHolderType = holder as PostViewHolder
        viewHolderType.bind(item)
    }

    // getItemCount 메서드에서 데이터의 개수를 반환합니다.
    override fun getItemCount(): Int {
        return postList.size
    }

    inner class PostViewHolder(val binding: ActivityPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                val context = itemView.context
                val postData = postList[position]

                val intent = Intent(context, PostDetailActivity::class.java)
                intent.putExtra("title", postData.title)
                intent.putExtra("content", postData.content)
                intent.putExtra("image", postData.image)
                intent.putExtra("user_age", postData.user_age)
                intent.putExtra("user_nickName", postData.user_nickName)
                intent.putExtra("user_profile", postData.user_profile)
                intent.putExtra("user_mbti", postData.user_mbti)
                intent.putExtra("user_gender", postData.user_gender)
                intent.putExtra("time", postData.time)
                context.startActivity(intent)
            }
        }

        fun bind(postData: PostData) = binding.apply {
            binding.PostItemTitle.text = postData.title
            binding.PostItemTime.text = postData.time
            binding.PostItemTxtUserNickname.text = postData.user_nickName

            val storage = FirebaseStorage.getInstance()
            val reference = storage.getReference("images/${postData.image}")
            reference.downloadUrl.addOnSuccessListener {
                Log.d("bind", "image${postData.image}")
                Glide.with(binding.root)
                    .load(it)
                    .into(PostItemImgPost)
            }
            val profileReference = storage.getReference("images/${postData.user_profile}")
            profileReference.downloadUrl.addOnSuccessListener {
                Log.d("bind", "image${postData.user_profile}")
                Glide.with(binding.root)
                    .load(it)
                    .into(PostItemImgUserprofile)
            }

            val postsRef = FirebaseData.database.getReference("posts")
            binding.PostItemLike.setOnClickListener {
                postData.likeByUser = !postData.likeByUser

                if (postData.likeByUser) {
                    postData.likeCount++
                    binding.PostItemLike.setImageResource(R.drawable.ic_post_like_on)
                    Toast.makeText(itemView.context, "좋아요를 눌렀습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    postData.likeCount--
                    binding.PostItemLike.setImageResource(R.drawable.ic_post_like_off)
                    Toast.makeText(itemView.context, "좋아요를 취소했습니다.", Toast.LENGTH_SHORT).show()
                }
                val postId = postData.user_uid+postData.time
                postsRef.child(postId).child("likeByUser").setValue(postData.likeByUser)
                postsRef.child(postId).child("likeCount").setValue(postData.likeCount)

            }
        }
    }
    fun updateList(newList: List<PostData>) {
        postList = newList
        notifyDataSetChanged()
    }
    fun getLikedPosts(): List<PostData> {
        return postList.filter { it.likeByUser }
    }
}
