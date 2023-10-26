package com.example.mbti_talk.post

import android.content.Intent
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.mbti_talk.databinding.ActivityPostListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PostFragment : Fragment() {
    lateinit var binding: ActivityPostListBinding
    private val postList = mutableListOf<PostData>()
    private lateinit var postAdapter: PostAdapter
    private lateinit var userDB: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityPostListBinding.inflate(inflater, container, false)
        userDB = Firebase.database.reference.child("posts")
        postAdapter = PostAdapter(postList)

        // 리사이클러뷰, 어뎁터 연결
        binding.recyclerview.adapter = postAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        // 글쓰기 버튼을 클릭 했을 경우 ContentWriteActivity로 이동한다.
        binding.contentWriteBtn.setOnClickListener {
            val intent = Intent(requireContext(), PostWriteActivity::class.java)
            startActivity(intent)
        }
        // 데이터베이스에서 데이터 읽어오기

        userDB.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = mutableListOf<PostData>()
                for (data in snapshot.children) {
                    val item = data.getValue(PostData::class.java)
                    if (item != null) {
                        tempList.add(item)
                    }
                }
                postList.clear()
                postList.addAll(tempList)
                postAdapter.notifyDataSetChanged()
                postList.reverse()
                for (item in postList) {
                    Log.d("ImageData", "Image URL: ${item.image}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
        userDB.child("Users")
        return binding.root
    }
}

