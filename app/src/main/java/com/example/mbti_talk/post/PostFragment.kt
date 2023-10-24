package com.example.mbti_talk.post

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mbti_talk.R

import com.example.mbti_talk.databinding.ActivityPostListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class PostFragment : Fragment() {
    lateinit var binding: ActivityPostListBinding
    private val postList = mutableListOf<PostData>()
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityPostListBinding.inflate(inflater, container, false) // 수정된 부분

        // layoutManager 설정
        // LinearLayoutManager을 사용하여 수직으로 아이템을 배치한다.
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        // db에서 데이터를 받아와서 Adapter에 넣어준다.
        postAdapter = PostAdapter(postList)
        binding.recyclerview.adapter = postAdapter
        // 글쓰기 버튼을 클릭 했을 경우 ContentWriteActivity로 이동한다.
        binding.contentWriteBtn.setOnClickListener {
            val intent = Intent(requireContext(), PostWriteActivity::class.java)
            startActivity(intent)
        }
        // 데이터베이스에서 데이터 읽어오기
        getFBContentData()
        return binding.root
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