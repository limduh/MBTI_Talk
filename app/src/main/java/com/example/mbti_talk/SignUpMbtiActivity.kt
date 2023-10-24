package com.example.mbti_talk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mbti_talk.Main.MainActivity
import com.example.mbti_talk.databinding.ActivityMbtiInputBinding
import com.example.mbti_talk.post.PostActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpMbtiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMbtiInputBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMbtiInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectMBTI = binding.MbtiBox

        selectMBTI.setOnClickListener {
            val intent = Intent(this, SelectionMbtiActivity::class.java)
            startActivity(intent)
        }
        val combineMbti = intent.getStringExtra("COMBINED_MBTI")

        if (combineMbti == null) {
            binding.MbtiBtnLogin.visibility = View.GONE
        } else {
            combineMbti?.let {
                val mbtiValues = it.chunked(1)
                binding.MbtiTxtBox1.text = mbtiValues.getOrNull(0)
                binding.MbtiTxtBox2.text = mbtiValues.getOrNull(1)
                binding.MbtiTxtBox3.text = mbtiValues.getOrNull(2)
                binding.MbtiTxtBox4.text = mbtiValues.getOrNull(3)
            }
            binding.MbtiTxtExplanatoryText.text = getExplanatoryText(combineMbti)

            firebaseAuth = FirebaseAuth.getInstance()
            val database = FirebaseDatabase.getInstance().reference

            binding.MbtiBtnLogin.setOnClickListener {
                val uId = firebaseAuth.currentUser?.uid
                if (uId != null) {
                    val userRef = database.child("Users").child(uId)

                    userRef.child("user_mbti").setValue(combineMbti).addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "MBTI가 업데이트 되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this,PostActivity::class.java)
                        startActivity(intent)
                    }
                        .addOnFailureListener { error ->
                            Toast.makeText(this, "MBTI 값 업데이트 중 오류 발생: $error", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
        }
    }
    private fun getExplanatoryText(combinedMbti: String?): String {
        return when (combinedMbti) {
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