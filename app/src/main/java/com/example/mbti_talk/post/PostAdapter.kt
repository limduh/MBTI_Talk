package com.example.mbti_talk.post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mbti_talk.R
import com.example.mbti_talk.databinding.ActivityPostItemBinding


class PostAdapter(private val postList: List<PostData>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    // onCreateViewHolder 메서드에서 뷰홀더를 생성합니다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ActivityPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    // onBindViewHolder 메서드에서 뷰홀더에 데이터를 바인딩합니다.
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(postList[position])
    }

    // getItemCount 메서드에서 데이터의 개수를 반환합니다.
    override fun getItemCount(): Int {
        return postList.size
    }
    inner class PostViewHolder(val binding: ActivityPostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(postData: PostData) {
            binding.PostItemTitle.text = postData.title
            binding.PostItemContent.text = postData.content
            binding.PostItemTime.text = postData.time
//            binding.PostItemImgPost.
        }
    }
}