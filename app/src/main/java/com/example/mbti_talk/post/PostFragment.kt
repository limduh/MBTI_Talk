package com.example.mbti_talk.post

import android.content.Intent
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mbti_talk.R

import com.example.mbti_talk.databinding.ActivityPostListBinding
import com.example.mbti_talk.utils.Utils
import com.google.firebase.auth.FirebaseAuth
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
    private lateinit var postDB: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityPostListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postDB = Firebase.database.reference.child("posts")
        postAdapter = PostAdapter(postList)

        // 리사이클러뷰, 어뎁터 연결
        binding.recyclerview.adapter = postAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        binding.postListSearchBtn.setOnClickListener {
            val searchText = binding.postListSearch.text.toString().trim()
            val filteredList = filterPosts(searchText) // 검색 결과를 가져옴
            postAdapter.updateList(filteredList) // 어댑터에 업데이트된 결과 전달

        }

        val spinner = binding.postSpinner

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.post_spinner,
            R.layout.custom_spinner_item
        )

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                when (selectedItem) {
                    "모든게시물" -> loadAllPosts()
                    "나의게시물" -> loadMyPosts()
                    "좋아요한게시물" -> loadLikedPosts()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        // 글쓰기 버튼을 클릭 했을 경우 ContentWriteActivity로 이동한다.
        binding.contentWriteBtn.setOnClickListener {
            val intent = Intent(requireContext(), PostWriteActivity::class.java)
            startActivity(intent)
        }


        // 데이터베이스에서 데이터 읽어오기
        postDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = mutableListOf<PostData>()
                for (data in snapshot.children) {
                    val item = data.getValue(PostData::class.java)
                    if (item != null) {
                        tempList.add(item)
                    }
                }
                tempList.sortByDescending { it.time }
                postList.clear()
                postList.addAll(tempList)
                postAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterPosts(query: String): List<PostData> {
        if (query.isBlank()) {
            return postList // 검색어가 비어있으면 전체 목록 반환
        }

        val filteredList = mutableListOf<PostData>()
        for (data in postList) {
            if (data.user_nickName.contains(query, true) || data.title.contains(query, true)) {
                filteredList.add(data) // 검색어와 일치하는 게시물 추가
            }
        }
        return filteredList
    }
    private fun loadAllPosts() {
        postAdapter.updateList(postList)
    }

    private fun loadMyPosts() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUserUid != null) {
            val myPosts = postList.filter { it.user_uid == currentUserUid }
            postAdapter.updateList(myPosts)
        }

    }
    private fun loadLikedPosts() {
        val likedPosts = postAdapter.getLikedPosts()


    }
}


