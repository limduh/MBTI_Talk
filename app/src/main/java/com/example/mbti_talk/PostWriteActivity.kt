package com.example.mbti_talk

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mbti_talk.databinding.ActivityPostWriteBinding

class PostWriteActivity: AppCompatActivity() {

    lateinit var binding: ActivityPostWriteBinding
    private var prevX = 0f
    private var prevY = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}