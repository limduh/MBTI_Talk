package com.example.mbti_talk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mbti_talk.databinding.ActivityMbtiInputBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpMbtiActivity:AppCompatActivity() {
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


    }
}