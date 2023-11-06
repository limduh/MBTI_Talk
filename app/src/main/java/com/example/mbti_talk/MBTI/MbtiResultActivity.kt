package com.example.mbti_talk.MBTI

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mbti_talk.Main.BottomActivity
import com.example.mbti_talk.MyProfile.MyProfileFragment
import com.example.mbti_talk.R
import com.example.mbti_talk.post.FirebaseData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Locale

class MbtiResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mbti_result)

        val results = intent.getIntegerArrayListExtra("results") ?: arrayListOf()  // 1,2,1,1

        val resultTypes = listOf(
            listOf("E", "I"),
            listOf("S", "N"),
            listOf("T", "F"),
            listOf("J", "P")
        )

        var resultString = ""
        for (i in results.indices) {
            resultString += resultTypes[i][results[i] - 1]
        }

        val tv_resValue: TextView = findViewById(R.id.tv_resValue)
        tv_resValue.text = resultString

        val iv_ResImg: ImageView = findViewById(R.id.iv_resImg)

        //INFJ  -> ic_infj

        val imageResource = resources.getIdentifier("ic_${resultString.toLowerCase(Locale.ROOT)}", "drawable", packageName)

        iv_ResImg.setImageResource(imageResource)



        val btn_end: Button = findViewById(R.id.btn_res_end)
        btn_end.setOnClickListener {

            val intent = Intent(this, BottomActivity::class.java)
            intent.putExtra("startFragment", "MyProfileFragment") // 시작 Fragment 정보를 전달
            startActivity(intent)

            val uId = FirebaseAuth.getInstance().currentUser?.uid

            if (uId != null) {
                val userRef = FirebaseDatabase.getInstance().getReference("Users").child(uId)
                userRef.child("user_mbti").setValue(resultString).addOnSuccessListener {
                    Toast.makeText(this, "MBTI가 업데이트 되었습니다.", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { error ->
                    Toast.makeText(this, "MBTI 값 업데이트 중 오류 발생: $error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}