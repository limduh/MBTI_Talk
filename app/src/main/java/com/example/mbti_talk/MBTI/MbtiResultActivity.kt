package com.example.mbti_talk.MBTI

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mbti_talk.Main.BottomActivity
import com.example.mbti_talk.MyProfile.MyProfileFragment
import com.example.mbti_talk.R
import java.util.Locale

class MbtiResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mbti_result)

        val results = intent.getIntegerArrayListExtra("results") ?: arrayListOf()  // 1,2,1,1

        val resultTypes = listOf(
            listOf("E", "I"),
            listOf("N", "S"),
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

        val btn_retry: Button = findViewById(R.id.btn_res_retry)
        btn_retry.setOnClickListener {

            val intent = Intent(this, BottomActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("startFragment", "MyProfileFragment") // 시작 Fragment 정보를 전달
            startActivity(intent)
        }
    }
}