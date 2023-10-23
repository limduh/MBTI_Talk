package com.example.mbti_talk.post

import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mbti_talk.PostData
import com.example.mbti_talk.databinding.ActivityPostWriteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PostWriteActivity: AppCompatActivity() {

    lateinit var binding: ActivityPostWriteBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    val storage = Firebase.storage("gs://mbti-talk-f2a04.appspot.com")
    private var prevX = 0f
    private var prevY = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val btnBack = binding.postbackarrow
        btnBack.setOnClickListener {
            finish()
        }
        firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().reference

        binding.postSave.setOnClickListener {
            val uid = firebaseAuth.currentUser?.uid
            val title = binding.postTitle.text.toString()
            val content = binding.postEtContent.text.toString()
            val time = getTime()
            val combinedpost = "$uid$title$content$time"
            if (uid != null) {
                val userRef = database.child("Users").child(uid)

                userRef.child("post").setValue(combinedpost)

                    Toast.makeText(this, "게시글 입력 완료", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "게시글을 저장하는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

        binding.postImage.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 이미지 터치 시 이동을 시작합니다.
                    prevX = event.rawX
                    prevY = event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    // 이미지를 이동합니다.
                    val deltaX = event.rawX - prevX
                    val deltaY = event.rawY - prevY
                    val newX = v.x + deltaX
                    val newY = v.y + deltaY

                    // 이미지가 화면을 벗어나지 않도록 제한합니다.
                    val maxX = binding.postConstContent.width - v.width
                    val maxY = binding.postConstContent.height - v.height
                    v.x = Math.min(maxX.toFloat(), Math.max(0f, newX))
                    v.y = Math.min(maxY.toFloat(), Math.max(0f, newY))

                    prevX = event.rawX
                    prevY = event.rawY
                }
            }
            true
        }
    }
    fun getTime(): String {
        val currentDateTime = Calendar.getInstance().time
        val dateFormat =
            SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDateTime)

        return dateFormat
    }
//    fun addItem(user:PostData):String {
//        val id = FirebaseData.mydata.push().key!!
//        constructor()
//        constructor(title: String, content: String, image: String) {
//            this.title = title
//            this.con = maintext
//            this.image = image
//        }
//    }

}
class User {

}