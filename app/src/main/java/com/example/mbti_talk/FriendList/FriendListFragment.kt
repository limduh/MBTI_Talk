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
import com.example.mbti_talk.databinding.FragmentFriendFindBinding
import com.example.mbti_talk.databinding.FragmentFriendListBinding
import com.example.mbti_talk.utils.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FriendListFragment : Fragment() {

    private lateinit var binding: FragmentFriendListBinding
    private lateinit var friendadapter: UserAdapter
    private val friendList: MutableList<UserData> = mutableListOf()
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
        friendDB = Firebase.database.reference // RDB 에 대한 ref 초기화하고 "Users" 노드로부터 친구 목록 데이터 가져옴
        friendadapter = UserAdapter(requireContext(), friendList) // Rv 에 사용될 어댑터 초기화

        // RecyclerView에 어댑터 설정
        binding.friendlistFragRv.adapter = friendadapter
        binding.friendlistFragRv.layoutManager = LinearLayoutManager(requireContext())


    }



    override fun onResume() {
        super.onResume()
        Log.d("FriendListFragment", "onResume")
        val currentUserUid = Utils.getMyUid(requireContext())
        loadFriends(currentUserUid.toString())
    }

    private fun loadFriends(currentUserUid: String) {
        friendDB.child("Friends").child(currentUserUid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("FirebaseDatabase", "loadFriends onDataChange")

                if (dataSnapshot.exists()) {
                    val size = dataSnapshot.children.count()
                    Log.d("FirebaseDatabase", "dataSnapshot.exists() size = $size")
                    var cnt = 0
                    for (friendUidSnapshot in dataSnapshot.children) {
                        val friendUid = friendUidSnapshot.key
                        if (friendUid != null) {
                            if(cnt==size-1)
                                loadFriendData(friendUid,true)
                            else
                                loadFriendData(friendUid,false)
                        }
                        cnt++
                    }
                } else {
                    Log.d("FirebaseDatabase", "No friends found for UID: $currentUserUid")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("FirebaseDatabase", "onCancelled", databaseError.toException())
            }
        })
    }

    private fun loadFriendData(friendUid: String, isLast:Boolean) {
        friendDB.child("Users").child(friendUid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(friendDataSnapshot: DataSnapshot) {
                if (friendDataSnapshot.exists()) {
                    val friendData = friendDataSnapshot.getValue(UserData::class.java)
                    if (friendData != null) {
                        friendList.add(friendData)
                        if(isLast)
                            friendadapter.notifyDataSetChanged()
                    }
                } else {
                    Log.d("FirebaseDatabase", "Friend data not found for UID: $friendUid")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("FirebaseDatabase", "onCancelled", databaseError.toException())
            }
        })
    }


}