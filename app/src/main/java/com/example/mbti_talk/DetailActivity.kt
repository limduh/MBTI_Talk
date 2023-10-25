package com.example.mbti_talk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.example.mbti_talk.Adapter.UserAdapter
import com.example.mbti_talk.Main.MainFriendActivity
import com.example.mbti_talk.databinding.ActivityDetailBinding
import com.example.mbti_talk.utils.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Constants
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class DetailActivity : AppCompatActivity() {

    // DetailActivity 클래스의 멤버 변수들을 선언
    lateinit var binding: ActivityDetailBinding // 뷰바인딩 초기화
    lateinit var detailDB: DatabaseReference // RDB 와 연동하기 위한 레퍼런스를 초기화

    // TextView 초기화
    private lateinit var nameTextView: AppCompatTextView
    private lateinit var ageTextView: AppCompatTextView
    private lateinit var genderTextView: AppCompatTextView
    private lateinit var mbtiTextView: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰 바인딩 초기화, 해당 바인딩을 현재 액티비티 레이아웃으로 설정
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TextView 초기화
        nameTextView = binding.DetailTxtNickname
        ageTextView = binding.DetailTxtAge
        genderTextView = binding.DetailTxtGender
        mbtiTextView = binding.DetailTxtMbti

        // "현재 사용자"의 UID 가져옴
        val myId = Utils.getMyUid(this)

        // MainActivity로부터 전달받은 intent를 통해 "선택한 사용자"의 UID를 가져옴
        val userID = intent.getStringExtra("userId")

        Log.d("DetailActivity", "My userID = $myId")
        Log.d("DetailActivity", "Selected userID = $userID")


        detailDB = Firebase.database.reference.child("Users") // RDB 초기화하고 "Users" 레퍼런스 가져오기

        // RDB 에서 사용자 데이터 가져오기 ( 주요 목적은 사용자 ID(uid)를 기반으로 DB에서 해당 user 정보를 찾아내고, 화면 표시 위해 userData에 저장. 한 번에 한 사용자의 정보만 가져옴.)

        detailDB.addListenerForSingleValueEvent(object : ValueEventListener { // RDB에서 데이터를 읽어오기 위한 리스너를 설정. 데이터의 한 번 읽기 작업을 수행
            override fun onDataChange(snapshot: DataSnapshot) { // 데이터를 성공적으로 읽어왔을 때 호출.snapshot은 데이터베이스에서 가져온 정보(=uid)
                Log.d("DetailActivity", "snapshot.exists() = ${snapshot.exists()}") // snapshot.exists()를 통해 스냅샷이 데이터를 포함하는지 여부를 확인

                if (snapshot.exists()) { // 데이터 스냅샷 존재 확인. 스냅샷이 데이터 포함 시 이 블록 안으로 진입
                    lateinit var userData: DataSnapshot

                    // 선택한 유저 데이터 찾기
                    if (userID != null) {
                        userData = snapshot.child(userID) // userID가 null이 아닌 경우, snapshot.child(userID)를 사용하여 snapshot에서 해당 사용자 ID에 해당하는 데이터 스냅샷을 가져옴. 이는 특정 사용자의 데이터를 나타내며, userData 변수에 저장

                        // 데이터에서 이름, 나이, 성별, MBTI 정보 가져오기 (RDB 에서 특정 유저 정보를 가져와 변수에 저장하는 부분. DB의 트리 구조와 각 데이터 유형에 맞게 데이터 뽑아옴)
                        val name = userData.child("user_nickName").getValue<String?>() // userData라는 DataSnapshot에서 "user_nickName"이라는 자식 경로에 있는 값을 가져옴.
                        val age = userData.child("user_age").getValue<Int?>() // 여기서는 user 데이터 아래에 nickname, age, gender, mbti 라는 자식 경로로 데이터가 저장되어있음
                        val gender = userData.child("user_gender").getValue<String?>()
                        val mbti = userData.child("user_mbti").getValue<String?>()

                        // 가져온 데이터를 TextView에 설정
                        nameTextView.text = name
                        ageTextView.text = age.toString()
                        genderTextView.text = gender
                        mbtiTextView.text = mbti

                    } else {
                        // 사용자 정보를 가져오지 못한 경우
                        Toast.makeText(this@DetailActivity, "사용자 정보를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // DB 가져오는 과정에서 오류 발생 시, 오류 메시지 출력
                Toast.makeText(this@DetailActivity, "데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        val friendAdd = binding.DetailBtnFriendAdd

        friendAdd.setOnClickListener {
            // RDB 의 "Friends" 레퍼런스에 사용자 uid 추가
            if (userID != null) {
                val friendsRef = Firebase.database.reference.child("Friends").child(myId.toString())
                friendsRef.child(userID).setValue(true)
            }
            // 클릭 이벤트 처리
            // Intent 사용하여 MainFriendActivity 로 이동
            val intent = Intent(this, MainFriendActivity::class.java)
            Toast.makeText(this@DetailActivity, "친구 추가가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }
}