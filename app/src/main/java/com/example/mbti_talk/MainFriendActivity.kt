package com.example.mbti_talk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mbti_talk.databinding.ActivityMainFriendBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainFriendActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainFriendBinding
    //    lateinit var adapter: FriendAdapter
    lateinit var friendList: MutableList<UserData> // 친구 데이터 목록

    private lateinit var friendDB: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainFriendBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main_friend)

        // 인증, DB, 리스트 adapter 초기화
        friendList = mutableListOf()
        friendDB = Firebase.database.reference.child("Users")

//        adapter = FriendAdapter(friendList) // 어댑터 초기화
    }
}