package com.example.mbti_talk.Main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mbti_talk.Adapter.FriendAdapter
import com.example.mbti_talk.R
import com.example.mbti_talk.UserData
import com.example.mbti_talk.databinding.ActivityMainFriendBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/* RDB 에서 데이터를 가져와 RecyclerView를 사용하여 화면에 표시
1. MainFriendActivity 클래스 정의
2. 뷰 바인딩을 초기화 -> RDB 에서 데이터를 가져와 목록에 표시
3. onCreate 메서드에서 RDB 로부터 데이터 가져옴.
4. 가져온 데이터를 friendList에 추가
5. RecyclerView 어댑터를 초기화 후 데이터 목록을 어댑터에 설정
6. 마지막으로, RDB 로부터 데이터를 비동기적으로 가져오고 가져오는 동안 발생한 오류를 처리.
* */
class MainFriendActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainFriendBinding

    lateinit var friendAdapter: FriendAdapter

    lateinit var friendList: MutableList<UserData> // 친구 데이터 목록

    private lateinit var friendDB: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰 바인딩 초기화, 해당 바인딩을 현재 액티비티 레이아웃으로 설정
        binding = ActivityMainFriendBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main_friend)

        // 친구 데이터 목록 및 RDB 초기화
        friendList = mutableListOf()
        friendDB = Firebase.database.reference.child("Users")

        // RecyclerView 어댑터 초기화
        friendAdapter = FriendAdapter(friendList)

        // RecyclerView 에 어댑터 설정
        binding.friendRv.adapter = friendAdapter
        // RecyclerView 에 레이아웃 매니저 설정
        binding.friendRv.layoutManager = LinearLayoutManager(this)

        // RDB 에서 친구 목록 데이터 가져오기
        friendDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // 데이터를 가져오는 과정에서 오류가 발생할 수기있으므로 try-catch 문 사용
                try {
                    // 가져온 데이터를 UserData 객체로 변환
                    for (snapshot in snapshot.children) {
                        val user = snapshot.getValue(UserData::class.java)
                        // User 객체를 friendList에 추가
                        friendList.add(user!!)
                    }
                    // 어댑터에 데이터가 추가되었음을 알림
                    friendAdapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // DB에서 데이터를 가져오는 도중 오류 발생 시, 오류 메시지 출력
                Toast.makeText(this@MainFriendActivity, "데이터를 가져오는데 실패했습니다.", Toast.LENGTH_LONG).show()
            }
        })
    }
}