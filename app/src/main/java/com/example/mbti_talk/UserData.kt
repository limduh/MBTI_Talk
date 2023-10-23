package com.example.mbti_talk

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


// FB 와 연결되어 있습니다.
@Parcelize
data class UserData(
    val user_email: String = "",
    val user_age: Int = 0,
    val user_nickName: String = "",
    val uid: String = "",
    val user_gender: String = "",
    val user_mbti: String = "",
    val user_profile: Int = 0,
    val user_addFriend: MutableList<String> = mutableListOf("", "", "", "", ""),
    val user_blockFriend: MutableList<String> = mutableListOf("", "", "", "", ""),
) : Parcelable

@Parcelize
data class PostData(
    val uid: String = "", // 게시물 고유 식별자
    val title: String = "",
    val content: String = "",
    val time: String = ""
) : Parcelable