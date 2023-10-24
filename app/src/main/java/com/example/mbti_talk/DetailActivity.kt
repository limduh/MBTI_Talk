package com.example.mbti_talk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.example.mbti_talk.Adapter.UserAdapter
import com.example.mbti_talk.databinding.ActivityDetailBinding
import com.example.mbti_talk.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Constants
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class DetailActivity : AppCompatActivity() {

    // DetailActivity 클래스의 멤버 변수들을 선언
    lateinit var binding: ActivityDetailBinding // 뷰바인딩 초기화
    lateinit var detailDB: DatabaseReference // FB Realtime DB와 연동하기 위한 레퍼런스를 초기화

    private lateinit var nameTextView: AppCompatTextView
    private lateinit var ageTextView: AppCompatTextView
    private lateinit var genderTextView: AppCompatTextView
    private lateinit var mbtiTextView: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰 바인딩 초기화, 해당 바인딩을 현재 액티비티 레이아웃으로 설정
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nameTextView = binding.DetailTxtNickname
        ageTextView = binding.DetailTxtAge
        genderTextView = binding.DetailTxtGender
        mbtiTextView = binding.DetailTxtMbti

        // MainActivity 에서 넘어온 intent 를 받아서 userID 변수에 저장
        val userID = intent.getStringExtra("userId")

        Log.d("DetailActivity", "userID = $userID")
        detailDB = Firebase.database.reference.child("Users") // FB Realtime DB 초기화하고 "Users" 레퍼런스 가져오기

        // RDB 에서 사용자 데이터 가져오기
        detailDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("DetailActivity", "snapshot.exists() = ${snapshot.exists()}")
                if (snapshot.exists()) {
                    lateinit var userData : DataSnapshot
                    if (userID != null) {
                        userData = snapshot.child(userID)
                    } else {
                        Toast.makeText(this@DetailActivity, "사용자 정보를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                    // 데이터에서 이름, 나이, 성별, MBTI 정보 가져오기
                    val name = userData.child("user_nickName").getValue<String?>()
                    val age = userData.child("user_age").getValue<Int?>()
                    val gender = userData.child("user_gender").getValue<String?>()
                    val mbti = userData.child("user_mbti").getValue<String?>()

                    // 가져온 데이터를 TextView에 설정
                    nameTextView.text = name
                    ageTextView.text = age.toString()
                    genderTextView.text = gender
                    mbtiTextView.text = mbti
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // DB 가져오는 과정에서 오류 발생 시, 오류 메시지 출력
                Toast.makeText(this@DetailActivity, "데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}