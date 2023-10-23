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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PostWriteActivity: AppCompatActivity() {

    lateinit var binding: ActivityPostWriteBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var prevX = 0f
    private var prevY = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FirebaseAuth, Realtime DB 초기화
        firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().getReference("Users")
        val btnBack = binding.postbackbtn
        btnBack.setOnClickListener {
            finish()
        }

        binding.postSave.setOnClickListener {
            val title = binding.postTitle.text.toString()
            val content = binding.postText.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val time = getTime()
                val postId = database.push().key
                if (postId != null) {
                    val post = PostData(postId, title, content, time)
                    database.child(postId).setValue(post)

                    Toast.makeText(this, "게시글 입력 완료", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "게시글을 저장하는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
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
                    val maxX = binding.postPage.width - v.width
                    val maxY = binding.postPage.height - v.height
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
}