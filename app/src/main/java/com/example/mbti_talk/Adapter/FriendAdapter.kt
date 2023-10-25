package com.example.mbti_talk.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mbti_talk.DetailActivity
import com.example.mbti_talk.Profile
import com.example.mbti_talk.UserData
import com.example.mbti_talk.databinding.UserListBinding
import com.example.mbti_talk.databinding.FriendListBinding
import kotlinx.coroutines.NonDisposableHandle.parent

// 이 어댑터는 RecyclerView에 사용됨. 각 아이템은 사용자의 친구 정보를 나타내며, 유저가 리스트 클릭 시, 해당 유저의 프로필 화면으로 이동 기능 추가

class FriendAdapter (private val mContext: Context, private val friendList: List<UserData>) : RecyclerView.Adapter<FriendAdapter.Holder>() {

    // onCreateViewHolder 함수는 ViewHolder 객체를 생성, 초기화
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        // FriendListBinding.inflate() 함수를 통해 XML 레이아웃 파일에서 뷰를 inflate, 그 뷰를 사용하여 Holder 객체를 생성 -> 친구 목록이 화면에 표시됨.
        val binding = FriendListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val friend_list = binding.clListFriend // 친구 리스트 바인딩

        // 친구 리스트 클릭 시 프로필로 이동
        friend_list.setOnClickListener {

            // startActivity 함수를 사용하여 Friend Detail 로 이동하는 인텐트를 생성하고 실행. 이 경우 DetailActivity::class.java로 지정된 Profile 로 이동
            val intent = Intent(parent.context, DetailActivity::class.java)
            startActivity(parent.context, intent, null)
        }

        return Holder(binding)
    }

    // 목록에 있는 리스트 수를 반환
    override fun getItemCount(): Int {
        return friendList.size
    }

    // 친구 목록을 화면에 표시할 때 호출. 이 함수에서 사용자 정보를 뷰홀더에 설정
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val friend = friendList[position]

        // 아이템 데이터 설정
        holder.friend_nickname.text = friend.user_nickName
        holder.friend_age.text = friend.user_age.toString()
        holder.friend_gender.text = friend.user_gender
        holder.friend_mbti.text = friend.user_mbti
//        holder.friend_profile.setImageResource(friend.user_profile)

        // friend list 클릭 시 DetailActivity 로 이동
        holder.friend_list.setOnClickListener {
            // startActivity 함수를 사용하여 User Detail 로 이동하는 인텐트를 생성하고 실행. 이 경우 DetailActivity::class.java로 지정된 Profile 로 이동
            val intent = Intent(mContext, DetailActivity::class.java)
            // 클릭한 user data 를 DetailActivity 로 전달
            intent.putExtra("userId", friend.user_uid)
            startActivity(mContext, intent, null)
        }
    }

    // RecyclerView.ViewHolder 를 확장한 클래스. 각 목록에 대한 뷰 요소 저장
    inner class Holder(binding: FriendListBinding) : RecyclerView.ViewHolder(binding.root) {
        val friend_list = binding.clListFriend // 친구 리스트 바인딩
        val friend_nickname = binding.etFriendNickName // 닉네임 텍스트뷰
        val friend_age = binding.etFriendAge // 나이 텍스트뷰
        val friend_gender = binding.etFriendGender // 성별 텍스트뷰
        val friend_mbti = binding.etFriendMbti // mbti 텍스트뷰
        val friend_profile = binding.ivFriendProfile // 프로필 이미지뷰
    }

}