package com.example.mbti_talk.post

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.mbti_talk.databinding.ActivityPostListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class PostActivity : AppCompatActivity() {
    lateinit var binding: ActivityPostListBinding
    private val postList = mutableListOf<PostData>()
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getStringExtra("POSTLIST")

        // layoutManager 설정
        // LinearLayoutManager을 사용하여 수직으로 아이템을 배치한다.
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        val intent = Intent(this, PostActivity::class.java)
        intent.putExtra("POSTLIST",id)
        postAdapter = PostAdapter(postList)
        binding.recyclerview.adapter = postAdapter
        // 글쓰기 버튼을 클릭 했을 경우 ContentWriteActivity로 이동한다.
        binding.contentWriteBtn.setOnClickListener {
            startActivity(Intent(this, PostWriteActivity::class.java))
        }
        // 데이터베이스에서 데이터 읽어오기
        getFBContentData()
    }
    private fun getFBContentData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()

                for (data in snapshot.children) {
                    val item = data.getValue(PostData::class.java)
                    if (item != null) {
                        postList.add(item)
                    }
                }
                postAdapter.notifyDataSetChanged()
                postList.reverse()
                val postAdapter = PostAdapter(postList)
                binding.recyclerview.adapter = postAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
    }


}