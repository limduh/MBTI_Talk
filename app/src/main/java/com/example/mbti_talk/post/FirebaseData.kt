package com.example.mbti_talk.post

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseData {
    companion object {
        val database = Firebase.database("https://mbti-talk-f2a04-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val mydata = database.getReference("posts")
    }
}