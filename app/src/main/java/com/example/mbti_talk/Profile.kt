package com.example.mbti_talk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mbti_talk.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Profile : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //FB초기화
        firebaseAuth = FirebaseAuth.getInstance()

        //로그인한 유저의 UID
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val currentUserId = currentUser.uid


            //DB에서 현재 사용자의 데이터 가져오기
        val usersReference = FirebaseDatabase.getInstance().getReference("Users")
        usersReference.child(currentUserId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(UserData::class.java)
                    if (userData != null) {
                        binding.ProfileEmail.text = "${userData.user_email}"
                        binding.ProfileTxtUid.text = "${userData.uid}"
                        binding.ProfileNickname.text = "${userData.user_nickName}"
                        binding.ProfileAge.text = "${userData.user_age}"
                        binding.ProfileGender.text = "${userData.user_gender}"
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // 데이터 가져오기 실패 시 처리
            }
        })
        }
    }
}
