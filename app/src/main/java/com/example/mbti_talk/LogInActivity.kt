package com.example.mbti_talk

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mbti_talk.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val etxtId = binding.LoginEtxtId
        val etxtPassword = binding.LoginEtxtPassword
        val btnLogin = binding.LoginBtnLogin

        btnLogin.setOnClickListener {
            val id = etxtId.text.toString()
            val password = etxtPassword.text.toString()

            if (id.isEmpty() || password.isEmpty()) {
                // 아이디 또는 비밀번호가 입력X
                Toast.makeText(this, "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 아이디와 비밀번호가 모두 입력O
                if (resultLogin(id, password)) {
                    // 로그인 성공 토스팅
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    // 메인 페이지 이동
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // 로그인 화면 종료
                } else {
                    // 로그인 실패
                    Toast.makeText(this, "아이디 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // 로그인 화면 종료
            }
        }
    }
}

fun resultLogin(id: String, password: String): Boolean {
    return true
}