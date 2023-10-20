package com.example.mbti_talk.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mbti_talk.Profile
import com.example.mbti_talk.UserData
import com.example.mbti_talk.databinding.UserListBinding

// 이 어댑터는 RecyclerView에 사용됨. 각 아이템은 사용자 정보를 나타내며, 유저가 리스트 클릭 시, 해당 유저의 프로필 화면으로 이동 기능 추가

class UserAdapter(private val userList: List<UserData>) : RecyclerView.Adapter<UserAdapter.Holder>() {


    // onCreateViewHolder 함수는 ViewHolder 객체를 생성, 초기화
    // ItemBinding.inflate() 함수를 통해 XML 레이아웃 파일에서 뷰를 inflate, 그 뷰를 사용하여 Holder 객체를 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val user_list = binding.clListItem // 유저 리스트 바인딩

        // 유저 리스트 클릭 시 프로필로 이동
        user_list.setOnClickListener {
            val intent = Intent(parent.context, Profile::class.java)
            startActivity(parent.context, intent, null)
        }

        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user = userList[position]

        // 아이템 데이터 설정
        holder.user_nickname.text = user.user_nickName
        holder.user_age.text = user.user_age.toString()
        holder.user_gender.text = user.user_gender
        holder.user_mbti.text = user.user_mbti
        holder.user_profile.setImageResource(user.user_profile)

    }

    // Holder 클래스는 ViewHolder 패턴을 구현
    // XML 레이아웃에서 정의한 뷰들을 멤버 변수로 가진다.
    // 이 뷰들에 유저 데이터 설정, 아이템 이벤트를 처리
    inner class Holder(binding: UserListBinding) : RecyclerView.ViewHolder(binding.root) {

        val user_nickname = binding.etNickName // 닉네임 텍스트뷰
        val user_age = binding.etAge // 나이 텍스트뷰
        val user_gender = binding.etGender // 성별 텍스트뷰
        val user_mbti = binding.etMbti // mbti 텍스트뷰
        val user_profile = binding.ivProfile // 프로필 이미지뷰
    }
}