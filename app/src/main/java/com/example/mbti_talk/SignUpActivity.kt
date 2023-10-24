package com.example.mbti_talk


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mbti_talk.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


// Firebase 를 사용하여 사용자 회원가입 처리, Realtime DB에 유저 정보 저장

class SignUpActivity : AppCompatActivity() {

    // 뷰바인딩
    private lateinit var binding: ActivitySignUpBinding

    // Authentication 초기화
    private lateinit var firebaseAuth: FirebaseAuth

    // 파이어베이스 데이터베이스 초기화
    private lateinit var database: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 뷰바인딩 초기화
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FirebaseAuth, Realtime DB 초기화
        firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().getReference("Users")
        val btnBack = binding.SignUpBtnBack
        btnBack.setOnClickListener {
            finish()
        }

        //라디오버튼 누르면 이곳으로 값을 전해준다.
        var user_gender = ""

        //라디오버튼 누를때 로직
        binding.SignUpRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.SignUp_radioMale -> user_gender = "남자"
                R.id.SignUp_radioFemale -> user_gender = "여자"
            }
        }





        binding.SignUpBtnSignUp.setOnClickListener {
            // 유저가 입력한 회원가입 정보 가져오기
            val SignupActivity_id = binding.SignUpEtxtID.text.toString()
            val SignupActivity_pass = binding.SignUpEtxtPw.text.toString()
            val SignupActivity_confirmPass = binding.SignUpEtxtPwCheck.text.toString()
            val SignupActivity_age = binding.SignUpEtxtAge.text.toString().toInt()
            val SignupActivity_nickName = binding.SignUpEtxtNickName.text.toString()





            if (SignupActivity_id.isNotEmpty() && SignupActivity_pass.isNotEmpty() && SignupActivity_confirmPass.isNotEmpty()) {
                // 비밀번호 일치 여부 확인
                if (SignupActivity_pass == SignupActivity_confirmPass) {
                    firebaseAuth?.createUserWithEmailAndPassword(
                        SignupActivity_id,
                        SignupActivity_pass
                    )
                        ?.addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "계정 생성 완료.", Toast.LENGTH_SHORT).show()
                                val userId = firebaseAuth.currentUser?.uid
                                if (userId != null) {
                                    Log.d("SignUp","#dudu+ $user_gender")
                                    val user = UserData(SignupActivity_id, SignupActivity_age, SignupActivity_nickName, userId, user_gender )
                                    // DB저장
                                    database.child(userId).setValue(user)
                                }

                                val intent = Intent(this, LogInActivity::class.java)

                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "계정 생성 실패", Toast.LENGTH_SHORT).show()

                            }

                        }
                } else {
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()

                }
            }
            else{
                Toast.makeText(this, "작성하지 않은곳이 있네요 !!", Toast.LENGTH_SHORT).show()
            }
            return@setOnClickListener

        }
    }
}

