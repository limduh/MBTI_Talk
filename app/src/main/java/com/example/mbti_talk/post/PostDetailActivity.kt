package com.example.mbti_talk.post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.mbti_talk.DetailActivity
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
        val nickname = intent.getStringExtra("user_nickName")
        val profile = intent.getStringExtra("user_profile")

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
        binding.PostDetailUserpicture.setOnClickListener {
            val userId = intent.getStringExtra("userId")
            val userAge = intent.getIntExtra("user_age", -1) // 추가: 유저 나이 가져오기 (기본값 -1로 설정)
            val usergender = intent.getStringExtra("user_gender")
            val usermbti = intent.getStringExtra("user_mbti")

            // DetailActivity로 이동하는 Intent를 생성합니다.
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("user_Nickname", nickname) // 추가: 유저 닉네임 전달
            intent.putExtra("user_age", userAge) // 추가: 유저 나이 전달
            intent.putExtra("user_profile", profile)
            intent.putExtra("user_gender", usergender)
            intent.putExtra("user_mbti", usermbti)

            startActivity(intent)
        }


        binding.PostDetailBackarrow.setOnClickListener {
            finish()
        }
    }
}