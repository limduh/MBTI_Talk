package com.example.mbti_talk

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mbti_talk.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val etxtId = binding.LoginEtxtId
        val etxtPassword = binding.LoginEtxtPassword
        val btnLogin = binding.LoginBtnLogin
        val btnSignUp = binding.loginTxtSignUp
        firebaseAuth = FirebaseAuth.getInstance()
        val get_email = intent.getStringExtra("email")
        val get_password = intent.getStringExtra("password")
        val etxtFindPw = binding.loginTxtFindPW

        //회원가입시 바로 로그인 정보 입력
        etxtId.setText(get_email)
        etxtPassword.setText(get_password)

        // 로그인 버튼 클릭 시
        btnLogin.setOnClickListener {
            val id = etxtId.text.toString()
            val password = etxtPassword.text.toString()

            if (id.isEmpty() || password.isEmpty()) {
                // 아이디 또는 비밀번호가 입력X
                Toast.makeText(this, "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                resultLogin(id, password)
            }
        }

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }


        //비밀번호 재설정
        etxtFindPw.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_forgot, null)
            val userEmail = view.findViewById<EditText>(R.id.editBox)
            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnReset).setOnClickListener {
                compareEmail(userEmail)
                dialog.dismiss()
            }
            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun resultLogin(id: String, pass: String) {
        firebaseAuth.signInWithEmailAndPassword(id, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공 토스팅
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    // 메인 페이지 이동
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // 로그인 화면 종료
                } else {
                    //로그인 실패
                    Toast.makeText(this, "아이디 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //Outside onCreate
    private fun compareEmail(email: EditText){
        if (email.text.toString().isEmpty()){
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }
        firebaseAuth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "적어주신 email로 링크를 보냈습니다.", Toast.LENGTH_LONG).show()
                }
            }
    }
}








