package com.example.mbti_talk


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mbti_talk.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
////////
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.SignUpBtnSignUp.setOnClickListener {
            val SignupActivity_id = binding.SignUpEtxtID.text.toString()
            val SignupActivity_pass = binding.SignUpEtxtPw.text.toString()
            val SignupActivity_confirmPass = binding.SignUpEtxtPwCheck.text.toString()


            if (SignupActivity_id.isNotEmpty() && SignupActivity_pass.isNotEmpty() && SignupActivity_confirmPass.isNotEmpty()) {
                if (SignupActivity_pass == SignupActivity_confirmPass) {

                    Toast.makeText(this, "회원가입이 완료되었습니다.로그인 해주세요", Toast.LENGTH_SHORT).show()
                    firebaseAuth.createUserWithEmailAndPassword(SignupActivity_id, SignupActivity_pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, LogInActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "작성하지 않은곳이 있네요 !!", Toast.LENGTH_SHORT).show()

            }
        }
    }
}