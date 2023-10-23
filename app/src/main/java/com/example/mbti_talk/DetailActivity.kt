package com.example.mbti_talk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mbti_talk.Adapter.UserAdapter
import com.example.mbti_talk.databinding.ActivityDetailBinding
import com.example.mbti_talk.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Constants
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DetailActivity : AppCompatActivity() {

    // DetailActivity 클래스의 멤버 변수들을 선언
    lateinit var binding: ActivityDetailBinding // 뷰바인딩 초기화

    lateinit var detailDB: DatabaseReference // FB Realtime DB와 연동하기 위한 레퍼런스를 초기화

    private lateinit var dataList: MutableList<UserData> // 유저 목록을 저장 위한 MutableList를 초기화. 유저 데이터는 RecyclerView 에 표시됨.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰 바인딩 초기화, 해당 바인딩을 현재 액티비티 레이아웃으로 설정
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailDB = Firebase.database.reference.child("Users") // FB Realtime DB 초기화하고 "Users" 레퍼런스 가져오기

        // RDB 에서 사용자 데이터 가져오기
        detailDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                // 가져온 데이터를 UserData 객체로 변환
                for (snapshot in snapshot.children) {
                    val user = snapshot.getValue(UserData::class.java)
                    // 가져온 데이터를 UserData 객체로 변환 후, userList 에 추가
                    if (user != null) {
                        dataList.add(user)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

                // DB 가져오는 과정에서 오류 발생 시, 오류 메시지 출력
                Toast.makeText(this@DetailActivity, "데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        // Intent 에서 userPosition 받음
        val userDetail = intent.getParcelableExtra<UserData>("userDetail") as UserData

        // userData 를 사용하여 Detail 정보 표시
        // ex) nickName 표시하는 TextView 를 찾아서 설정
        val detailNickname = binding.DetailTxtNickname
        detailNickname.text = userDetail.user_nickName

        val detailMbti = binding.DetailTxtMbti
        detailMbti.text = userDetail.user_mbti

        val detailAge = binding.DetailTxtAge
        detailAge.text = userDetail.user_age.toString()
    }
}