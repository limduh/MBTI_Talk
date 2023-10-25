package com.example.mbti_talk.FriendList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mbti_talk.Adapter.UserAdapter
import com.example.mbti_talk.UserData
import com.example.mbti_talk.databinding.FragmentFriendListBinding
import com.example.mbti_talk.utils.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// RDB 사용하여 친구목록을 가져와 RecyclerView에 표시
class FriendListFragment : Fragment() {

    private lateinit var binding: FragmentFriendListBinding
    private lateinit var friendadapter: UserAdapter
    private val friendList: MutableList<UserData> = mutableListOf()
    private lateinit var userDB: DatabaseReference
    private lateinit var friendDB: DatabaseReference

    // onCreateView 함수는 Fragment가 생성될 때 호출. Fragment의 사용자 인터페이스 레이아웃을 초기화
    override fun onCreateView(
        /* inflater 매개변수는 XML 레이아웃 파일을 Fragment의 레이아웃으로 확장
        * container 매개변수는 Fragment가 표시될 부모 뷰 그룹*/
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // XML 레이아웃을 화면에 그리기 위해 바인딩 객체 생성
        binding = FragmentFriendListBinding.inflate(inflater, container, false)
        return binding.root
    }

    /* onCreateView 이후 호출
    * onCreateView에서 inflate 된 레이아웃에 대한 추가 작업을 수행
    * ex) view 에 data 채우거나 event 처리
    * super.onViewCreated(view, savedInstanceState)는 상위 클래스(부모 Fragment 또는 AppCompatActivity)의 onViewCreated 함수 호출.
    * 여기서 view는 onCreateView에서 반환한 뷰입니다
    * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("FriendListFragment", "onViewCreated")

        // 친구 데이터 목록 및 RDB 초기화
        userDB = Firebase.database.reference.child("Users")
        friendDB = Firebase.database.reference.child("Friends")

        val currentUserUid = Utils.getMyUid(requireContext()) // util 함수 통해 현재 사용자 uid 가져오기
        loadFriends(currentUserUid.toString()) // 현재 사용자 uid를 통해 친구 목록 가져오기

        friendadapter = UserAdapter(requireContext(), friendList) // Rv 에 사용될 어댑터 초기화

        // RecyclerView에 어댑터 설정
        binding.friendlistFragRv.adapter = friendadapter
        binding.friendlistFragRv.layoutManager = LinearLayoutManager(requireContext())
    }

    // 현재 사용자의 uid 기반으로 친구 목록을 로드하는 함수. 친구 수 계산 후 각 친구 uid 반복하여 'loadFriendData' 함수 사용하여 친구 데이터 불러옴. friendAdapter 는 모든 친구 데이터를 불러온 후 한번만 알림을 받음.
    private fun loadFriends(currentUserUid: String) {
        // 중복 로드 피하기 위해 friendList 를 지움.
        friendList.clear()

        // 현재 유저의 친구 데이터베이스를 쿼리합니다. (정보요청)
        friendDB
            .child(currentUserUid) // friendDb 아무래 currentUserUid 를 키로 갖는 하위 노드 찾음.
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
                            if(cnt==size-1)
                                loadFriendData(friendUid,true)
                            else
                                loadFriendData(friendUid,false)
                        }
                        cnt++ // 처리한 친구 수 추적 위해 cnt 변수 증가
                    }
                } else {
                    // 현재 사용자의 친구가 없다면, 친구를 찾을 수 없다는 로그 남김
                    Log.d("FirebaseDatabase", "No friends found for UID: $currentUserUid")
                    Toast.makeText(requireContext(), "친구를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
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
            override fun onDataChange(friendDataSnapshot: DataSnapshot) {
                if (friendDataSnapshot.exists()) {
                    // friendDataSnapshot에서 UserData 클래스로 데이터 변환
                    val friendData = friendDataSnapshot.getValue(UserData::class.java)
                    // 읽어온 친구 데이터를 friendList에 추가
                    if (friendData != null) {
                        friendList.add(friendData)
                        // 만약 마지막 친구 데이터를 가져왔다면, 어댑터에 변경을 알림.
                        if(isLast)
                            friendadapter.notifyDataSetChanged()
                    }
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