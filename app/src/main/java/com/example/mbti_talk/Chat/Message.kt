package com.example.mbti_talk.Chat

import java.io.Serializable

data class Message(
    var senderUid: String = "",
    var sended_date: String = "",
    var message: String = "",
    var confirmed:Boolean=false
): Serializable {
}
