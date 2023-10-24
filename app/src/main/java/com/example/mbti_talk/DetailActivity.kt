package com.example.mbti_talk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.example.mbti_talk.Adapter.UserAdapter
import com.example.mbti_talk.databinding.ActivityDetailBinding
import com.example.mbti_talk.databinding.ActivityMainBinding
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
    lateinit var detailDB: DatabaseReference // FB Realtime DB와 연동하기 위한 레퍼런스를 초기화

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

        // MainActivity 에서 넘어온 intent 를 받아서 userID 변수에 저장
        val userID = intent.getStringExtra("userId")

        Log.d("DetailActivity", "userID = $userID")
        detailDB = Firebase.database.reference.child("Users") // FB Realtime DB 초기화하고 "Users" 레퍼런스 가져오기

        // RDB 에서 사용자 데이터 가져오기 ( 주요 목적은 사용자 ID(uid)를 기반으로 DB에서 해당 user 정보를 찾아내고, 화면 표시 위해 userData에 저장. 한 번에 한 사용자의 정보만 가져옴.)

        detailDB.addListenerForSingleValueEvent(object : ValueEventListener { // RDB에서 데이터를 읽어오기 위한 리스너를 설정. 데이터의 한 번 읽기 작업을 수행
            override fun onDataChange(snapshot: DataSnapshot) { // 데이터를 성공적으로 읽어왔을 때 호출.snapshot은 데이터베이스에서 가져온 정보(=uid)
                Log.d("DetailActivity", "snapshot.exists() = ${snapshot.exists()}") // snapshot.exists()를 통해 스냅샷이 데이터를 포함하는지 여부를 확인

                if (snapshot.exists()) { // 데이터 스냅샷 존재 확인. 스냅샷이 데이터 포함 시 이 블록 안으로 진입
                    lateinit var userData : DataSnapshot

                    // 사용자 ID에 해당하는 데이터 찾기
                    if (userID != null) {
                        userData = snapshot.child(userID) // userID가 null이 아닌 경우, snapshot.child(userID)를 사용하여 snapshot에서 해당 사용자 ID에 해당하는 데이터 스냅샷을 가져옴. 이는 특정 사용자의 데이터를 나타내며, userData 변수에 저장
                    } else {
                        // 사용자 정보를 가져오지 못한 경우
                        Toast.makeText(this@DetailActivity, "사용자 정보를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }

                    // 데이터에서 이름, 나이, 성별, MBTI 정보 가져오기 (RDB 에서 특정 유저 정보를 가져와 변수에 저장하는 부분. DB의 트리 구조와 각 데이터 유형에 맞게 데이터 뽑아옴)
                    val name = userData.child("user_nickName").getValue<String?>() // userData라는 DataSnapshot에서 "user_nickName"이라는 자식 경로에 있는 값을 가져옴.
                    val age = userData.child("user_age").getValue<Int?>() // 여기서는 user 데이터 아래에 nickname, age, gender, mbti 라는 자식 경로로 데이터가 저장되어있음
                    val gender = userData.child("user_gender").getValue<String?>()
                    val mbti = userData.child("user_mbti").getValue<String?>()

                    /* Child 는 DB의 트리 구조에서 특정 노드 아래에 위치한 하위 노드.
                    ex) 유저 정보를 저장하는 "Users"라는 루트 노드가 있고, 각 사용자는 고유한 uid(자식 노드) 아래에 저장.
                    이 자식 노드들은 다시 "user_nickName", "user_age", "user_gender", "user_mbti"와 같은 하위 자식 노드를 가짐.
                    이 코드에서 child("user_nickName"), child("user_age"), child("user_gender"), child("user_mbti")를 사용하여 하위 자식 노드로 이동

                    getValue 함수는 특정 데이터 스냅샷에서 값 가져올 때 사용.
                    데이터의 형식 지정하고, 해당 형식으로 값을 반환.
                    여기서는 String 및 Int 형식으로 값을 가져옴. 값이 없는 경우 null 반환 위해 getValue 함수에 String? 및 Int?을 사용.*/



                    // 가져온 데이터를 TextView에 설정
                    nameTextView.text = name
                    ageTextView.text = age.toString()
                    genderTextView.text = gender
                    mbtiTextView.text = mbti
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // DB 가져오는 과정에서 오류 발생 시, 오류 메시지 출력
                Toast.makeText(this@DetailActivity, "데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}