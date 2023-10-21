package com.example.mbti_talk.Main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mbti_talk.Adapter.UserAdapter
import com.example.mbti_talk.UserData
import com.example.mbti_talk.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/* RDB 에서 데이터를 가져와 RecyclerView를 사용하여 화면에 표시
1. MainActivity 클래스 정의
2. 뷰 바인딩을 초기화 -> RDB 에서 데이터를 가져와 목록에 표시
3. onCreate 메서드에서 RDB 로부터 데이터 가져옴.
4. 가져온 데이터를 userList에 추가
5. RecyclerView 어댑터를 초기화 후 데이터 목록을 어댑터에 설정
6. 마지막으로, RDB로부터 데이터를 비동기적으로 가져오고 가져오는 동안 발생한 오류를 처리.
* */

class MainActivity : AppCompatActivity() {

    // MainActivity 클래스의 멤버 변수들을 선언
    lateinit var binding: ActivityMainBinding // 뷰바인딩 초기화

    lateinit var adapter: UserAdapter // RecyclerView 에 사용될 어댑터 초기화

    private lateinit var userList: MutableList<UserData> // 유저 목록을 저장 위한 MutableList를 초기화. 유저 데이터는 RecyclerView 에 표시됨.

    private lateinit var userDB: DatabaseReference // FB Realtime DB와 연동하기 위한 레퍼런스를 초기화

    companion object {
//        const val HOME_ITEM = R.id.homeFragment  서치프래그먼트없음
//        const val OFFERS_ITEM = R.id.offersFragment
//        const val MORE_ITEM = R.id.moreFragment
//        const val SECTION_ITEM = R.id.sectionFragment
//        const val CART_ITEM = R.id.cartFragment
//        const val BLANK_ITEM = R.id.blankFragment 바텀에 연결할 페이지가 없음
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 뷰 바인딩 초기화, 해당 바인딩을 현재 액티비티 레이아웃으로 설정
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인증, DB, 리스트 adapter 초기화
        userList = mutableListOf() // 사용자 데이터 목록 초기화 (이 데이터는 FB 에서 가져옴)
        userDB =
            Firebase.database.reference.child("Users") // FB Realtime DB 초기화하고 "Users" 레퍼런스 가져오기
        adapter = UserAdapter(userList) // RecyclerView 어댑터 초기화

        // RecyclerView와 어댑터 연결
        binding.mainRv.adapter = adapter
        binding.mainRv.layoutManager = LinearLayoutManager(this)

        // RDB 에서 사용자 데이터 가져오기
        userDB.limitToFirst(30).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userSnapshot in snapshot.children) {
                    // 각 사용자의 데이터를 UserData 객체로 가져와 목록 추가
                    val user = userSnapshot.getValue(UserData::class.java)
                    if (user != null) {
                        userList.add(user)
                    }
                }
                adapter.notifyDataSetChanged() // 어댑터에게 데이터가 변경되었음을 알립니다.
            }

            override fun onCancelled(error: DatabaseError) {
                // 처리 중 오류 발생 시 토스트 표시
                Toast.makeText(this@MainActivity, "데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}