package com.example.mbti_talk

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mbti_talk.databinding.ActivityMbtiInputBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpMbtiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMbtiInputBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMbtiInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectMBTI = binding.MbtiBox

        selectMBTI.setOnClickListener {
            val intent = Intent(this, SelectionMbtiActivity::class.java)
            startActivity(intent)
        }
       val combineMbti = intent.getStringExtra("COMBINED_MBTI")

        combineMbti?.let {
            val mbtiValues = it.chunked(1)
            binding.MbtiTxtBox1.text = mbtiValues.getOrNull(0)
            binding.MbtiTxtBox2.text = mbtiValues.getOrNull(1)
            binding.MbtiTxtBox3.text = mbtiValues.getOrNull(2)
            binding.MbtiTxtBox4.text = mbtiValues.getOrNull(3)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().reference

        binding.MbtiBtnLogin.setOnClickListener {
            val uId = firebaseAuth.currentUser?.uid
            if (uId != null) {
                val userRef = database.child("Users").child(uId)

                userRef.child("mbti").setValue(combineMbti).addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "MBTI가 업데이트 되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                    .addOnFailureListener { error ->
                        Toast.makeText(this, "MBTI 값 업데이트 중 오류 발생: $error", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }
    }
}