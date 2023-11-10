package nb_.mbti_talk.MBTI

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nb_.mbti_talk.databinding.ActivityMbtiSelectionboxBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpMbtiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMbtiSelectionboxBinding

    // Authentication 초기화
    private lateinit var firebaseAuth: FirebaseAuth

    // 파이어베이스 데이터베이스 초기화
    private lateinit var database: DatabaseReference

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


            val intent = Intent(this, MbtiActivity::class.java)
            intent.putExtra("COMBINED_MBTI", combinedMbti)
            startActivity(intent)


        }
    }
}