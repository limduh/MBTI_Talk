package com.example.mbti_talk

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mbti_talk.databinding.ActivityMbtiSelectionboxBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SelectionMbtiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMbtiSelectionboxBinding

    // Authentication 초기화
    private lateinit var firebaseAuth: FirebaseAuth

    // 파이어베이스 데이터베이스 초기화
    private lateinit var database: DatabaseReference
    private var selectedMbtiOptions = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMbtiSelectionboxBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        binding.MbtiBtnEnter.setOnClickListener {
            saveMbtiToFirebase()
        }
    }

    private fun saveMbtiToFirebase() {

        val selectedEI = when (binding.MbtiSelecbox1.checkedRadioButtonId) {
            binding.MbtiSelecboxE.id -> "E"
            binding.MbtiSelecboxI.id -> "I"
            else -> ""
        }

        // SN 그룹 처리
        val selectedSN = when (binding.MbtiSelecbox2.checkedRadioButtonId) {
            binding.MbtiSelecboxS.id -> "S"
            binding.MbtiSelecboxN.id -> "N"
            else -> ""
        }

        // TF 그룹 처리
        val selectedTF = when (binding.MbtiSelecbox3.checkedRadioButtonId) {
            binding.MbtiSelecboxT.id -> "T"
            binding.MbtiSelecboxF.id -> "F"
            else -> ""
        }

        // JP 그룹 처리
        val selectedJP = when (binding.MbtiSelecbox4.checkedRadioButtonId) {
            binding.MbtiSelecboxJ.id -> "J"
            binding.MbtiSelecboxP.id -> "P"
            else -> ""
        }
        if (selectedEI.isNotEmpty() && selectedSN.isNotEmpty() && selectedTF.isNotEmpty() && selectedJP.isNotEmpty()) {
            val combinedMbti = "$selectedEI$selectedSN$selectedTF$selectedJP"

            val uId = firebaseAuth.currentUser?.uid
            if (uId != null) {
                val userRef = database.child("Users").child(uId)

                userRef.child("mbti").setValue(combinedMbti).addOnSuccessListener {
                    Toast.makeText(this, "MBTI가 입력되었습니다!", Toast.LENGTH_SHORT).show()

                    selectedMbtiOptions.clear()
                }
                    .addOnFailureListener { error ->
                        Toast.makeText(this, "MBTI 값 업데이트 중 오류 발생: $error", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }
    }
}