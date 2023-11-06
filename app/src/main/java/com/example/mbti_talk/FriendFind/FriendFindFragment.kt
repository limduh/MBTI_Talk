package com.example.mbti_talk.FriendFind

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mbti_talk.Adapter.UserAdapter
import com.example.mbti_talk.DetailActivity
import com.example.mbti_talk.Main.FilterDialogFragment
import com.example.mbti_talk.R
import com.example.mbti_talk.UserData
import com.example.mbti_talk.databinding.FragmentFriendFindBinding
import com.example.mbti_talk.utils.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FriendFindFragment : Fragment() {
    private lateinit var binding: FragmentFriendFindBinding
    private lateinit var adapter: UserAdapter
    private val userList: MutableList<UserData> = mutableListOf()
    private lateinit var userDB: DatabaseReference
    lateinit var mContext: Context

    // onCreateView 함수는 Fragment가 생성될 때 호출. Fragment의 사용자 인터페이스 레이아웃을 초기화
    override fun onCreateView(
        /* inflater 매개변수는 XML 레이아웃 파일을 Fragment의 레이아웃으로 확장
        * container 매개변수는 Fragment가 표시될 부모 뷰 그룹*/
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // XML 레이아웃을 화면에 그리기 위해 바인딩 객체 생성
        binding = FragmentFriendFindBinding.inflate(inflater, container, false)
        return binding.root
    }

    /* onAttach 함수: Fragment가 Activity에 연결되었을 때 호출되는 콜백 메서드
    이 메서드를 통해 Fragment는 연결된 Activity의 context 가져올 수 있음 */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context // mContext 변수에 context 할당
    }

    /* onCreateView 이후 호출
    * onCreateView에서 inflate 된 레이아웃에 대한 추가 작업을 수행
    * ex) view 에 data 채우거나 event 처리
    * super.onViewCreated(view, savedInstanceState)는 상위 클래스(부모 Fragment 또는 AppCompatActivity)의 onViewCreated 함수 호출.
    * 여기서 view는 onCreateView에서 반환한 뷰 */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("FriendFindFragment", "onViewCreated")


        // RecyclerView 및 어댑터 초기화
        userDB = Firebase.database.reference.child("Users")
        adapter = UserAdapter({
            // 클릭한 user data 를 DetailActivity 로 전달
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("userId", it) // uid 줌
            intent.putExtra("viewtype", "Find") // 키값 find 줌
            startActivity(intent)
        })

        // RecyclerView에 어댑터 설정
        binding.FriendFindFragRv.adapter = adapter
        binding.FriendFindFragRv.layoutManager = LinearLayoutManager(requireContext())


        // 사용자 데이터를 RDB 에서 가져오기
        val currentUserUid = Utils.getMyUid(requireContext())

        fun applyFilterConditions(ageCondition: Int?, genderCondition: String?, mbtiCondition: String?) {
            Utils.saveFilterConditions(requireContext(), ageCondition, genderCondition, mbtiCondition)

            val (filteredAge, filteredGender, filteredMbti) = Utils.getFilterConditions(requireContext())

            val filteredList = userList.filter { user ->
                val ageMatches = filteredAge?.let { user.user_age >= it } ?: true
                val genderMatches = filteredGender?.let { user.user_gender.equals(it) } ?: true
                val mbtiMatches = filteredMbti?.let { user.user_mbti == it } ?: true

                ageMatches && genderMatches && mbtiMatches
            }
            userList.clear()
            userList.addAll(filteredList)
            adapter.notifyDataSetChanged()

        }


        userDB
            .orderByChild("user_age")
            .limitToFirst(30).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Data 가져오기 성공 시 실행
                for (userSnapshot in snapshot.children) {
                    // 각 유저 정보를 UserData 객체로 받아오기
                    val user = userSnapshot.getValue(UserData::class.java)
                    // 사용자 본인 정보는 친구찾기 페이지에 표시되지 않음.
                    if (user != null && user.user_uid != currentUserUid) {

                        // Utils 에서 저장한 compat 을 불러오기
                        val compat = Utils.getCompat(Utils.getMyMbti(mContext), user.user_mbti) // 유저 MBTI와 친구 MBTI를 비교하여 compat 변수에 등급 할당
                        user.user_compat = compat.toString() //해당 등급 문자열로 저장

                        Log.d("friendfind", "myMbti=${Utils.getMyMbti(mContext)} otherMbti=${user.user_mbti} compat=${user.user_compat})")
                        userList.add(user) // user data 목록에 추가
                    }
                    userList.sortBy { it.user_compat } // userList라는 사용자 목록을 user_compat 기준으로 오름차순 정렬
                }
                adapter.setList(userList)
                adapter.notifyDataSetChanged() // 어댑터에게 데이터 변경을 알림
            }

            override fun onCancelled(error: DatabaseError) {
                // 처리 중 오류 발생 시 토스트 표시
                Toast.makeText(requireContext(), "해당 데이터는 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        // filter_btn을 찾아 클릭 이벤트를 처리
        val filterButton = view.findViewById<AppCompatImageButton>(R.id.filter_btn)
        filterButton.setOnClickListener {
            // FilterDialogFragment를 표시
            val filterDialog = FilterDialogFragment()

            filterDialog.show(childFragmentManager, "FilterDialog")

            val usrNewList = userList.filter { it.user_mbti.contains("I") && it.user_age >20 && it.user_age <30 && it.user_gender.equals("남자")}
            adapter.setList(usrNewList)

        }
    }
}