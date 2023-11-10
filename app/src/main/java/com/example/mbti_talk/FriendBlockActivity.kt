package com.example.mbti_talk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import nb_.mbti_talk.Adapter.UserAdapter
import nb_.mbti_talk.DetailActivity
import nb_.mbti_talk.R
import nb_.mbti_talk.UserData
import nb_.mbti_talk.databinding.ActivityDetailBinding
import nb_.mbti_talk.databinding.ActivityFriendBlockBinding
import nb_.mbti_talk.databinding.FragmentFriendListBinding
import nb_.mbti_talk.utils.Utils

class FriendBlockActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFriendBlockBinding
    private lateinit var friendadapter: UserAdapter
    private val friendList: MutableList<UserData> = mutableListOf()
    private lateinit var userDB: DatabaseReference
    private lateinit var friendDB: DatabaseReference
    private val userBlockList: MutableList<String> = mutableListOf()
    private lateinit var friendBlockDB: DatabaseReference
    lateinit var mContext: Context
    lateinit var currentUserUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        binding = ActivityFriendBlockBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("FriendBlockActivity", "onViewCreated")

        // 친구 데이터 목록 및 RDB 초기화
        userDB = Firebase.database.reference.child("Users")
        friendDB = Firebase.database.reference.child("Friends")
        friendBlockDB = Firebase.database.reference.child("Friends_block")

        // RecyclerView 및 어댑터 초기화
        userDB = Firebase.database.reference.child("Users")
        friendadapter = UserAdapter({
            deleteFriendBlockEntry(it)
        })
        friendadapter.setList(friendList)

        // RecyclerView에 어댑터 설정
        binding.friendblocklistFragRv.adapter = friendadapter
        binding.friendblocklistFragRv.layoutManager = LinearLayoutManager(this)

        currentUserUid = Utils.getMyUid(this).toString() // util 함수 통해 현재 사용자 uid 가져오기

        // "뒤로가기" 버튼 클릭 이벤트 처리
        binding.blockBtnBack.setOnClickListener {
            finish() // 다이얼로그 닫기
        }

    }


    fun deleteFriendBlockEntry(uid:String) {
        // Delete the child
        friendBlockDB.child(currentUserUid).child(uid).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // The deletion is successful
                Log.d("Firebase", "deleteFriendBlockEntry jblee Entry deleted successfully. uid = ${uid}")

                friendList.removeAll { userData -> userData.user_uid == uid }
                Log.d("Firebase", "deleteFriendBlockEntry jblee friendList size = ${friendList.size}")

                friendadapter.setList(friendList)
                friendadapter.notifyDataSetChanged()

            } else {
                // The deletion failed
                Log.d("Firebase", "Failed to delete entry: ${task.exception?.message}")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val currentUserUid = Utils.getMyUid(this) // util 함수 통해 현재 사용자 uid 가져오기
        loadBlockFriends(currentUserUid.toString()) // 현재 사용자 uid를 통해 친구 목록 가져오기
    }

    // 현재 사용자의 uid 기반으로 친구 목록을 로드하는 함수. 친구 수 계산 후 각 친구 uid 반복하여 'loadFriendData' 함수 사용하여 친구 데이터 불러옴. friendAdapter 는 모든 친구 데이터를 불러온 후 한번만 알림을 받음.
    private fun loadBlockFriends(currentUserUid: String) {
        // 중복 로드 피하기 위해 friendList 를 지움.
        friendList.clear()


        // 현재 유저의 친구 데이터베이스를 쿼리합니다. (정보요청)
        friendBlockDB
            .child(currentUserUid) // friendDb 아래 currentUserUid 를 키로 갖는 하위 노드 찾음.
            .addListenerForSingleValueEvent(object : ValueEventListener { // alfsv 함수는 데이터 변경을 단 한번만 기다림.
                override fun onDataChange(dataSnapshot: DataSnapshot) { // RDB 에서 데이터 검색 성공 시 실행되는 콜백 함수.
                    Log.d("FirebaseDatabase", "loadFriends onDataChange")

                    // 추가한 친구 uid 가 userDB 에 존재하는지 확인
                    if (dataSnapshot.exists()) {
                        // 유저의 친구 수 계산. datasnapshot = 현재 사용자의 친구 목록 데이터
                        val size = dataSnapshot.children.count()
                        // 로그에 친구 수 출력
                        Log.d("FirebaseDatabase", "dataSnapshot.exists() size = $size")
                        var cnt = 0
                        // 각 하위 노드(친구 uid) 반복 처리
                        for (friendUidSnapshot in dataSnapshot.children) {
                            // 하위 노드에서 친구 uid 추출
                            val friendUid = friendUidSnapshot.key
                            // 친구 uid가 null이 아닌지 확인. null 이면 데이터 안부름.
                            if (friendUid != null) {
                                // 각 친구 uid에 loadFriendData 함수를 통해 친구 데이터를 가져옴.
                                // true, false 는 마지막 친구 데이터를 가져왔는지 확인하기 위한 변수.
                                // friendadaper 가 모든 친구 데이터 불러온 후 한번만 알림을 받게 함.
                                if(cnt==size-1) {
                                    Log.d("FirebaseDatabase", "loadFriendData isLast = true")
                                    loadFriendData(friendUid, true)
                                }else {
                                    Log.d("FirebaseDatabase", "loadFriendData isLast = false")
                                    loadFriendData(friendUid, false)
                                }
                            }
                            cnt++ // 처리한 친구 수 추적 위해 cnt 변수 증가
                        }
                    } else {
                        // 현재 사용자의 친구가 없다면, 친구를 찾을 수 없다는 로그 남김
                        Log.d("FirebaseDatabase", "No friends found for UID: $currentUserUid")
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) { // DB 오류 처리하고 메시지 로깅
                    Log.d("FirebaseDatabase", "onCancelled", databaseError.toException())
                }
            })
    }

    // 친구 uid를 통해 친구 데이터를 가져오는 함수
    private fun loadFriendData(friendUid: String, isLast:Boolean) {
        // userDB 에서 frienUid 에 해당하는 데이터 조회
        userDB
            .child(friendUid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(friendSnapshot: DataSnapshot) {
                    if (friendSnapshot.exists()) {

                        // friendSnapshot에서 UserData 클래스로 데이터 변환
                        val friend = friendSnapshot.getValue(UserData::class.java)
                        // 읽어온 친구 데이터를 friendList에 추가
                        if (friend != null) {

                            // Utils 에서 저장한 Compat 을 불러오기
                            val compat = Utils.getCompat(Utils.getMyMbti(mContext),friend.user_mbti) // 유저 MBTI와 친구 MBTI를 비교하여 compat 변수에 등급 할당
                            friend.user_compat = compat.toString() //해당 등급 문자열로 저장


                            var isBlock = false
                            for (list in userBlockList){
                                if(friend.user_uid.equals(list)){
                                    isBlock = true ;
                                    break
                                }
                            }

                            Log.d("friendList", "myMbti=${Utils.getMyMbti(mContext)} otherMbti=${friend.user_mbti} compat=${friend.user_compat})")
                            if(!isBlock)
                                friendList.add(friend) // friend 목록에 추가

                            // 만약 마지막 친구 데이터를 가져왔다면, 어댑터에 변경을 알림.
                            if(isLast) {
                                friendadapter.setList(friendList)
                                friendadapter.notifyDataSetChanged()
                            }
                        }
                        friendList.sortBy { it.user_compat } // friendList 라는 사용자 목록을 user_compat 기준으로 오름차순 정렬
                    } else {
                        // 해당 친구 데이터가 존재하지 않을 경우, 로그에 메시지 기록
                        Log.d("FirebaseDatabase", "Friend data not found for UID: $friendUid")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // 데이터베이스 조회가 실패한 경우 오류 로그 기록
                    Log.d("FirebaseDatabase", "onCancelled", databaseError.toException())
                }
            })
    }

}