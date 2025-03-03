package nb_.mbti_talk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import nb_.mbti_talk.MBTI.MbtiActivity
import nb_.mbti_talk.Main.BottomActivity
import nb_.mbti_talk.databinding.ActivityLogInBinding
import nb_.mbti_talk.utils.Utils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LogInActivity : AppCompatActivity() {

    //구글로그인 클라이언트
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var binding: ActivityLogInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userDB: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val etxtId = binding.LoginEtxtId
        val etxtPassword = binding.LoginEtxtPassword
        val btnLogin = binding.LoginBtnLogin
        val btnSignUp = binding.loginTxtSignUp
        val etxtFindPw = binding.loginTxtFindPW

        binding.llLoading.setOnClickListener{

        }
        //구글 이미지 버튼
        val imageViewGoogle = binding.LoginImageViewGoogle
        //구글로그인 옵션
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        userDB = Firebase.database.reference.child("Users")


        //구글 이미지 누를때 이벤트
        imageViewGoogle.setOnClickListener {
            binding.llLoading.visibility = View.VISIBLE
            googleSignIn()

            Log.d("goolgestart","google")
        }


        // 로그인 버튼 클릭 시
        btnLogin.setOnClickListener {
            val id = etxtId.text.toString()
            val password = etxtPassword.text.toString()

            if (id.isEmpty() || password.isEmpty()) {
                // 아이디 또는 비밀번호가 입력X
                Toast.makeText(this, "아이디와 비밀번호를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                resultLogin(id, password)
            }
        }

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }


        //비밀번호 재설정
        etxtFindPw.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_forgot, null)
            val userEmail = view.findViewById<EditText>(R.id.editBox)
            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnReset).setOnClickListener {
                compareEmail(userEmail)
                dialog.dismiss()
            }
            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

    }

    private fun resultLogin(id: String, pass: String) {
        firebaseAuth.signInWithEmailAndPassword(id, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Firebase에 로그인한 정보 가져오고 해당 사용자 UID 얻음
                    val user = firebaseAuth.currentUser
                    val uid = user?.uid
                    Log.d("로그인 정보 가져옴", "UID ==== ${uid}")

                    // Utils 클래스를 사용하여 사용자의 UID를 저장
                    Utils.setMyUid(this, uid.toString())

                    // 로그인 성공 토스팅
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()


                    userDB.child(uid.toString())
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // 데이터가 존재하는 경우
                                    val userData = dataSnapshot.getValue(UserData::class.java)

                                    Log.d("FirebaseDatabase1", "User Data: $userData")

                                    if (!userData?.user_mbti.isNullOrEmpty()) {
                                        // userData?.user_mbti.toString()에서 유저 MBTI를 문자열로 변환. Utils.setMyMbti 함수를 호출하여 해당 MBTI 저장
                                        Utils.setMyMbti(this@LogInActivity, userData?.user_mbti.toString())
                                        val intent =
                                            Intent(this@LogInActivity, BottomActivity::class.java)
                                        startActivity(intent)
                                        finish() // 로그인 화면 종료
                                    } else {
                                        // 메인 페이지 이동
                                        val intent =
                                            Intent(this@LogInActivity, MbtiActivity::class.java)
                                        startActivity(intent)
                                        finish() // 로그인 화면 종료
                                    }

                                } else {
                                    // UID에 해당하는 데이터가 없는 경우
                                    Log.d("FirebaseDatabase", "User not found")
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // 쿼리가 취소되었을 때 호출됩니다.
                                Log.d(
                                    "FirebaseDatabase",
                                    "onCancelled",
                                    databaseError.toException()
                                )
                            }
                        })
                } else {
                    //로그인 실패
                    Toast.makeText(this, "아이디 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    //Outside onCreate
    private fun compareEmail(email: EditText) {
        if (email.text.toString().isEmpty()) {
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            return
        }
        firebaseAuth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "적어주신 email로 링크를 보냈습니다.", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    //구글로그인 함수
    private fun googleSignIn() {
        val signInClient = googleSignInClient.signInIntent
        launcher.launch(signInClient)
        Log.d("google1","#dudu ${signInClient}")
    }

    //구글 로그인 관련 런쳐
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                Log.d("google2","#dudu ${Activity.RESULT_OK}")
                manageResults(task);
            }
        }

    //구글 로그인 관련 함수
    private fun manageResults(task: Task<GoogleSignInAccount>) {

        val account: GoogleSignInAccount? = task.result

        if (account != null) {

            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
                if (task.isSuccessful) {
                    Log.d("google3","#dudu ")
                    val user = firebaseAuth.currentUser
                    val uid = user?.uid
                    Log.d("로그인 정보 가져옴", "UID ==== ${uid}")
                    // Utils 클래스를 사용하여 사용자의 UID를 저장
                    Utils.setMyUid(this, uid.toString())

                    Toast.makeText(this, "구글 로그인 성공!", Toast.LENGTH_SHORT).show()

                    userDB.child(uid.toString())
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // 데이터가 존재하는 경우
                                    val userData = dataSnapshot.getValue(UserData::class.java)

                                    Log.d("FirebaseDatabase1", "#dudu User Data: $userData")

                                    if (!userData?.user_mbti.isNullOrEmpty()) {
                                        Log.d("FirebaseDatabase2", "#dudu User Data: $userData")
                                        val intent =
                                            Intent(this@LogInActivity, BottomActivity::class.java)
                                        binding.llLoading.visibility = View.GONE
                                        startActivity(intent)
                                        finish() // 로그인 화면 종료
                                    } else {
                                        // 메인 페이지 이동
                                        Log.d("FirebaseDatabase3", "#dudu User Data: $userData")

                                        val intent =
                                            Intent(this@LogInActivity, MbtiActivity::class.java)
                                        binding.llLoading.visibility = View.GONE
                                        startActivity(intent)
                                        finish() // 로그인 화면 종료
                                    }

                                } else {
                                    // UID에 해당하는 데이터가 없는 경우
                                    Log.d("FirebaseDatabase4", "#dudu User not found")
                                    val intent =
                                        Intent(this@LogInActivity, SignUpActivity2::class.java)
                                    binding.llLoading.visibility = View.GONE
                                    startActivity(intent)
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // 쿼리가 취소되었을 때 호출됩니다.
                                Log.d(
                                    "FirebaseDatabase",
                                    "onCancelled",
                                    databaseError.toException()
                                )
                                binding.llLoading.visibility = View.GONE
                            }
                        })


                }
            }

        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

}










