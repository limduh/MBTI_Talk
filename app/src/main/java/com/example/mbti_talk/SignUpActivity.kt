package com.example.mbti_talk


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mbti_talk.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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

        binding.SignUpBtnSignUp.setOnClickListener {
            // 유저가 입력한 회원가입 정보 가져오기
//            val SignupActivity_id = binding.SignUpEtxtID.text.toString()
//            val SignupActivity_pass = binding.SignUpEtxtPw.text.toString()
//            val SignupActivity_confirmPass = binding.SignUpEtxtPwCheck.text.toString()
//            val SignupActivity_age = binding.SignUpEtxtAge.text.toString().toInt()
//            val SignupActivity_nickName = binding.SignUpEtxtNickName.text.toString()

            val SignupActivity_id = "test2@naver.com"
            val SignupActivity_pass = "sdjkdj"
            val SignupActivity_confirmPass = "sdjkdj"
            val SignupActivity_age = 23
            val SignupActivity_nickName = "실험"

            firebaseAuth?.createUserWithEmailAndPassword(SignupActivity_id, SignupActivity_pass)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this, "계정 생성 완료.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val userId = firebaseAuth.currentUser?.uid
                        if (userId != null) {

                            val user = UserData(
                                SignupActivity_id,
                                SignupActivity_age,
                                SignupActivity_nickName,
                                userId,
                                "남"
                            )
                            // DB저장
                            database.child(userId).setValue(user).addOnCompleteListener { dbTask -> if (dbTask.isSuccessful) {
                            // 데이터 저장 성공
                                Toast.makeText(
                                    this, "저장 성공.",
                                    Toast.LENGTH_SHORT
                                ).show()
                             } else {
                            // 실패
                                Toast.makeText(
                                    this, "저장 실패.",
                                    Toast.LENGTH_SHORT
                                ).show()
                              }
                            }
                        }




                    } else {
                        Toast.makeText(
                            this, "계정 생성 실패",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                    /*
            // UserData 객체 생성
//            val user = UserData(SignupActivity_id, SignupActivity_pass,SignupActivity_confirmPass, SignupActivity_age, SignupActivity_nickName, firebaseAuth.currentUser?.uid!!)
            val user = UserData(SignupActivity_id, SignupActivity_pass,SignupActivity_confirmPass, SignupActivity_age, SignupActivity_nickName, "")


            // Firebase Realtime DB에 유저 정보 저장
            database.child("users").child(SignupActivity_nickName).setValue(user)

            // 입력한 정보 유효성 검사
            if (SignupActivity_id.isNotEmpty() && SignupActivity_pass.isNotEmpty() && SignupActivity_confirmPass.isNotEmpty()) {
                // 비밀번호 일치 여부 확인
                if (SignupActivity_pass == SignupActivity_confirmPass) {
                    // firebase 를 사용한 회원가입 시도
                    firebaseAuth.createUserWithEmailAndPassword(SignupActivity_id, SignupActivity_pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            // 회원가입 성공 시 로그인 화면으로 이동
                            val intent = Intent(this, LogInActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this, "회원가입이 완료되었습니다.로그인 해주세요", Toast.LENGTH_SHORT).show()
                        } else {
                            // 회원가입 실패 시 실패 에러메시지 출력
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // 비밀번호 불일치 시 에러메시지 출력
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // 필수 정보 누락 시 메시지 표시
                Toast.makeText(this, "작성하지 않은곳이 있네요 !!", Toast.LENGTH_SHORT).show()
            }*/
                }
        }
    }