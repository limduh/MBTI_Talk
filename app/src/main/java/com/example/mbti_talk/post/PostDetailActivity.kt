package com.example.mbti_talk.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.mbti_talk.databinding.ActivityPostDetailBinding
import com.google.firebase.storage.FirebaseStorage

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val image = intent.getStringExtra("image")
        val nickname = intent.getStringExtra("nickname")
        val profile = intent.getStringExtra("profile")

        binding.PostDetailTitle.text = title
        binding.PostDetailEtContent.text = content
        binding.PostDetailUserName.text = nickname

        val storage = FirebaseStorage.getInstance()
        val reference = storage.reference.child("images/$image")
        reference.downloadUrl.addOnSuccessListener {
            Glide.with(this)
                .load(it)
                .into(binding.PostDetailImage)
        }
        val profileReference = storage.reference.child("images/$profile")
        profileReference.downloadUrl.addOnSuccessListener {
            Glide.with(this)
                .load(it)
                .into(binding.PostDetailUserpicture)
        }


        binding.PostDetailBackarrow.setOnClickListener {
            finish()
        }
    }
}