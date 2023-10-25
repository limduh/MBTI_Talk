package com.example.mbti_talk.post

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mbti_talk.R
import com.example.mbti_talk.databinding.ActivityPostItemBinding


class PostAdapter(private val postList: List<PostData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        fun bind(postData: PostData) = binding.apply {
            binding.PostItemTitle.text = postData.title
            binding.PostItemContent.text = postData.content
            binding.PostItemTime.text = postData.time
            Glide.with(binding.root)
                .load(Uri.parse(postData.image))
                .into(PostItemImgPost)
        }
    }
}