package com.example.mbti_talk.Chat

data class Message(
    var message:String?,
    var sendId:String?
){
    constructor():this("","")
}
