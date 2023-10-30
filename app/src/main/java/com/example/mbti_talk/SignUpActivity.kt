package com.example.mbti_talk


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mbti_talk.databinding.ActivitySignUpBinding
import com.example.mbti_talk.post.PostData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.regex.Pattern


// Firebase 를 사용하여 사용자 회원가입 처리, Realtime DB에 유저 정보 저장

class SignUpActivity : AppCompatActivity() {


    lateinit var selectedUri: Uri
    val storage = Firebase.storage("gs://mbti-talk-f2a04.appspot.com")

    ///사진 가져오는 권한요청 33
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permission33 = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_MEDIA_VIDEO,
    )

    ///사진 가져오는 권한요청 33이전버젼
    private val permission = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )


    // 뷰바인딩
    private lateinit var binding: ActivitySignUpBinding

    // Authentication 초기화
    private lateinit var firebaseAuth: FirebaseAuth

    // 파이어베이스 데이터베이스 초기화
    private lateinit var database: DatabaseReference

    var myProfileUri: Boolean = false


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 뷰바인딩 초기화
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FirebaseAuth, Realtime DB 초기화
        firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().getReference("Users")

        //back버튼
        val btnBack = binding.SignUpBtnBack
        btnBack.setOnClickListener {
            finish()
        }

        //이메일 체크
        emailFocusListener()
        //비밀번호 체크
        passwordFocusListener()
        //비밀번호 재확인
        passwordFocusListener2()


        //라디오버튼 누르면 이곳으로 값을 전해준다.
        var user_gender = ""

        //라디오버튼 누를때 로직
        binding.SignUpRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.SignUp_radioMale -> user_gender = "남자"
                R.id.SignUp_radioFemale -> user_gender = "여자"
            }
        }

        //사진기 이미지 누를때의 권한 설정
        binding.appCompatImageView5.setOnClickListener {
            Log.d("SignUp", "#dudu setOnClick appCompatImageView5")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(permission33, 100)
                else {
                    galleryLauncher.launch("image/*")
                }
            } else {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("SignUp", "#dudu setOnClick requestPermissions ")
                    requestPermissions(permission, 100)
                } else {
                    Log.d("SignUp", "#dudu setOnClick galleryLauncher")
                    galleryLauncher.launch("image/*")
                }

            }
        }





        binding.SignUpBtnSignUp.setOnClickListener {
            if (!myProfileUri) {
                Toast.makeText(this, "사진을 업로드하지 않았어요 !!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//            //최종확인 다이얼로그
//            val builder = AlertDialog.Builder(this)
//            val view = layoutInflater.inflate(R.layout.dialog_signup, null)
//            builder.setView(view)
//            val dialog = builder.create()
//
//            view.findViewById<Button>(R.id.SignUp_Dia_btn_check).setOnClickListener {
//                dialog.dismiss()
//                //그대로 가입 진행
//            }
//            view.findViewById<Button>(R.id.SignUp_Dia_btn_cancel).setOnClickListener {
//                dialog.dismiss()
//                //return to binding.SignUpBtnSignUp.setOnClickListener
//            }
//
//            dialog.show()


            // 유저가 입력한 회원가입 정보 가져오기
            val SignupActivity_id = binding.SignUpEtxtID.text.toString()
            val SignupActivity_pass = binding.SignUpEtxtPW.text.toString()
            val SignupActivity_confirmPass = binding.SignUpEtxtPW2.text.toString()
            val SignupActivity_age = binding.SignUpEtxtAge.text.toString().toInt()
            val SignupActivity_nickName = binding.SignUpEtxtNickName.text.toString()



            Log.d("Signup", "#dudu myProfileUrichack=$myProfileUri")

            if (SignupActivity_id.isNotEmpty() && SignupActivity_pass.isNotEmpty() && SignupActivity_confirmPass.isNotEmpty() )   {
                Log.d("Signup", "#dudu myProfileUrichack2=$myProfileUri")


                // 비밀번호 일치 여부 확인
                if (SignupActivity_pass == SignupActivity_confirmPass) {
                    firebaseAuth?.createUserWithEmailAndPassword(
                        SignupActivity_id,
                        SignupActivity_pass
                    )
                        ?.addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "계정 생성 완료.", Toast.LENGTH_SHORT).show()
                                if (selectedUri != null) {
                                    Log.d("vec", "Selected Image Uri: $selectedUri")
                                    uploadImage(selectedUri) {
                                        if (it != null) {

                                            Toast.makeText(
                                                this@SignUpActivity,
                                                "이미지 업로드 완료",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()

                                            val userId = firebaseAuth.currentUser?.uid
                                            if (userId != null) {
                                                Log.d("SignUp", "#dudu+ $user_gender")
                                                val user = UserData(
                                                    SignupActivity_id,
                                                    SignupActivity_age,
                                                    SignupActivity_nickName,
                                                    userId,
                                                    user_gender,
                                                    "",
                                                    it
                                                )//,SignUpActivity_uri )
                                                // DB저장
                                                database.child(userId).setValue(user)
                                            }

                                            val intent = Intent(this, LogInActivity::class.java)

                                            startActivity(intent)
                                        } else {
                                            Toast.makeText(
                                                this@SignUpActivity,
                                                "이미지 업로드 실패",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        this@SignUpActivity,
                                        "이미지를 선택해주세요",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }


                            } else {
                                Toast.makeText(this, "계정 생성 실패", Toast.LENGTH_SHORT).show()

                            }

                        }
                } else {
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(this, "작성하지 않은곳이 있어요 !!", Toast.LENGTH_SHORT).show()
            }
            return@setOnClickListener
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("SignUp", "#dudu onRequestPermissionsResult")
        Log.d("SignUp", "#dudu requestCode = $requestCode")
        Log.d("SignUp", "#dudu permissions = $permissions")
        Log.d("SignUp", "#dudu grantResults = ${grantResults[0]}")


        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                100 -> {
                    Log.d("SignUp", "#dudu permissionok = ${grantResults[0]}")
                    Toast.makeText(
                        this@SignUpActivity,
                        "권한 허용됨",
                        Toast.LENGTH_LONG
                    ).show()
                    galleryLauncher.launch("image/*")


                }
            }
        }
    }


    //이미지 갤러리 불러오기
    var galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        Log.d("SignIn", "dudu+ $uri")
        binding.appCompatImageView5.tag = uri
        binding.LoginImageView.setImageURI(uri)
        if (uri != null) {
            selectedUri = uri
            myProfileUri = true
            Log.d("Signup", "#dudu myProfileUripermission=$myProfileUri")
        }

    }

    fun uploadImage(uri: Uri, callback: (String?) -> Unit) {
        val fullPath = makeFilePath("images", "temp", uri)
        val imageRef = storage.getReference(fullPath)
        val uploadTask = imageRef.putFile(uri)

        // 업로드 실행 및 결과 확인
        uploadTask.addOnFailureListener {
            Log.d("Storage", "Fail -> ${it.message}")
            callback(null)
        }.addOnSuccessListener { taskSnapshot ->
            Log.d(
                "Storage",
                "Success Address -> ${taskSnapshot.metadata?.name}"
            )
            callback(taskSnapshot.metadata?.name)
        }
    }

    fun makeFilePath(path: String, userId: String, uri: Uri): String {
        val mimeType = contentResolver.getType(uri) ?: "/none" // MIME 타입 ex) images/jpeg
        val ext = mimeType.split("/")[1] // 확장자 ex) jpeg
        val timeSuffix = System.currentTimeMillis() // 시간값 ex) 1235421532
        val filename = "${path}/${userId}_${timeSuffix}.${ext}" // 완성!
        return filename
    }

    //이메일칸
    private fun emailFocusListener()
    {
        binding.SignUpEtxtID.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.SignUpConstEmail.helperText = validEmail()
            }
        }
    }
    //이메일 양식체크
    private fun validEmail(): String?
    {
        val emailText = binding.SignUpEtxtID.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            return "이메일 양식을 확인해 주세요"
        }
        return null
    }

    //비밀번호칸
    private fun passwordFocusListener()
    {
        binding.SignUpEtxtPW.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.SignUpConstPw.helperText = validPassword()
            }
        }
    }


    //비밀번호 양식체크
    private fun validPassword(): String?
    {
        val passwordText = binding.SignUpEtxtPW.text.toString()
        if(passwordText.length < 6)
        {
            return "6자 이상의 Password를 입력하세요"
        }
        if(!passwordText.matches(".*[A-Z].*".toRegex()))
        {
            return "1개 이상 대문자를 포함해 주세요"
        }
        if(!passwordText.matches(".*[a-z].*".toRegex()))
        {
            return "1개 이상의 소문자를 포함해 주세요"
        }
        if(!passwordText.matches(".*[!@#\$%^&*\\-+=].*".toRegex()))
        {
            return "1개 이상의 특수문자를 포함해 주세요ㅣ(!@#\$%^&*\\-+=)"
        }

        return null
    }

    //비밀번호 재확인칸
    private fun passwordFocusListener2()
    {
        binding.SignUpEtxtPW2.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.SignUpConstPw2.helperText = validPassword2()
            }
        }
    }

    //비밀번호 재확인 양식체크
    private fun validPassword2(): String?
    {
        val passwordText = binding.SignUpEtxtPW2.text.toString()
        if(passwordText.length < 6)
        {
            return "6자 이상의 Password를 입력하세요"
        }
        if(!passwordText.matches(".*[A-Z].*".toRegex()))
        {
            return "1개 이상 대문자를 포함해 주세요"
        }
        if(!passwordText.matches(".*[a-z].*".toRegex()))
        {
            return "1개 이상의 소문자를 포함해 주세요"
        }
        if(!passwordText.matches(".*[!@#\$%^&*\\-+=].*".toRegex()))
        {
            return "1개 이상의 특수문자를 포함해 주세요ㅣ(!@#\$%^&*\\-+=)"
        }

        return null
    }




}
