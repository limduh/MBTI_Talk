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

class Porfile : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val currentUserId = currentUser.uid

        firebaseAuth = FirebaseAuth.getInstance()

        val usersReference = FirebaseDatabase.getInstance().getReference("Users")
        usersReference.child(currentUserId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(UserData::class.java)
                    if (userData != null) {
                        binding.ProgileEmail.text = "${userData.user_email}"
                        binding.ProgileUid.text = "${userData.uid}"
                        binding.ProgileNickname.text = "${userData.user_nickName}"
                        binding.ProgileAge.text = "${userData.user_age}"
                        binding.ProgileGender.text = "${userData.gender}"
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
