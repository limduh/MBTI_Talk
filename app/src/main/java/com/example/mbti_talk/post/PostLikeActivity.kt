package com.example.mbti_talk.post

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mbti_talk.databinding.ActivityPostDetailBinding
import com.example.mbti_talk.databinding.ActivityPostLikeListBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PostLikeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostLikeListBinding
    private lateinit var postAdapter: PostAdapter
    private val postList = mutableListOf<PostData>()
    private lateinit var postDB: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostLikeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postDB = Firebase.database.reference.child("posts")
        postAdapter = PostAdapter(postList)

        val recyclerView = binding.postLikeRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = PostAdapter(emptyList())
        recyclerView.adapter = adapter

        binding.postLikeBackarrow.setOnClickListener {
            finish()
        }
    }
}