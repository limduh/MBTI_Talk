package com.example.mbti_talk.MBTI

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
        val tv_resString: TextView = findViewById(R.id.tv_resString)

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

        val explanatoryText = getExText(resultString)
        Log.d("Ex", explanatoryText)
        tv_resString.text = explanatoryText

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
    private fun getExText(resultString: String?): String {
        return when (resultString) {
            "ISTJ" -> "세상의 소금형\n\n한번 시작한 일은 끝까지 해내는 사람들"
            "ISFJ" -> "임금 뒷편의 권력형\n\n성실하고 온화하며 협조를 잘하는 사람들"
            "INFJ" -> "예연자형\n\n사람과 관련된 뛰어난 통찰력을 가지고 있는 사람들"
            "INTJ" -> "과학자형\n\n전체적인 부분을 조합하여 비전을 제시하는 사람들"
            "ISTP" -> "백과사전형\n\n논리적이고 뛰어난 상황 적응력을 가지고 있는 사람들"
            "ISFP" -> "성인군자형\n\n따뜻한 감성을 가지고 있는 겸손한 사람들"
            "INFP" -> "잔다르크형\n\n 이상적인 세상을 만들어 가는 사람들"
            "INTP" -> "아이디어 뱅크형\n\n비평적인 관점을 가지고 있는 뛰어난 전략가들"
            "ESTP" -> "수완좋은 활동가형\n\n친구, 운동, 음식 등 다양한 활동을 선호하는 사람들"
            "ESFP" -> "사교적인 유형\n\n분위기를 고조시키는 우호적인 사람들"
            "ENFP" -> "스파크형\n\n열정적으로 새로운 관계를 만드는 사람들"
            "ENTP" -> "발명가형\n\n풍부한 상상력을 가지고 새로운 것에 도전하는 사람들"
            "ESTJ" -> "사업가형\n\n사무적, 실용적, 현실적으로 일을 많이하는 사람들"
            "ESFJ" -> "친선도모형\n\n친절과 현실감을 바탕으로 타인에게 봉사하는 사람들"
            "ENFJ" -> "언변능숙형\n\n타인의 성장을 도모하고 협동하는 사람들"
            "ENTJ" -> "지도자형\n\n비전을 가지고 사람들을 활력적으로 이끌어가는 사람들"
            else -> ""
        }
    }
}