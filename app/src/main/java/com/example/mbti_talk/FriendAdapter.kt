package com.example.mbti_talk

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mbti_talk.databinding.UserListBinding
import com.example.mbti_talk.databinding.FriendListBinding
import kotlinx.coroutines.NonDisposableHandle.parent

//class FriendAdapter (private val friendList: List<UserData>) : RecyclerView.Adapter<FriendAdapter.Holder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.Holder {
//        val binding = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//
//        val user_list = binding.clListFriend // 친구 리스트 바인딩
//
//        // 친구 리스트 클릭 시 프로필로 이동
//        user_list.setOnClickListener {
//            val intent = Intent(parent.context, Profile::class.java)
//            startActivity(parent.context, intent, null)
//        }
//
//        return FriendAdapter.Holder(binding)
//    }
//
//}