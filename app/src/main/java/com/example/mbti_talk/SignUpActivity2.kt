package nb_.mbti_talk


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import nb_.mbti_talk.MBTI.MbtiActivity
import nb_.mbti_talk.databinding.ActivitySignUp2Binding
import nb_.mbti_talk.databinding.ActivitySignUpBinding
import nb_.mbti_talk.post.PostData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import nb_.mbti_talk.MyProfile.SignoutDialogChoice
import java.util.regex.Pattern


// Firebase 를 사용하여 사용자 회원가입 처리, Realtime DB에 유저 정보 저장

class SignUpActivity2 : AppCompatActivity() {


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
    private lateinit var binding: ActivitySignUp2Binding

    // Authentication 초기화
    private lateinit var firebaseAuth: FirebaseAuth

    // 파이어베이스 데이터베이스 초기화
    private lateinit var database: DatabaseReference

    var myProfileUri: Boolean = false


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 뷰바인딩 초기화
        binding = ActivitySignUp2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // FirebaseAuth, Realtime DB 초기화
        firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().getReference("Users")

        //back버튼
        val btnBack = binding.SignUpBtnBack
        btnBack.setOnClickListener {
            finish()
        }

        //닉네임 확인
        NickFocusListener()


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
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_signin, null)

            builder.setView(view)
            val dialog = builder.create()
            view.findViewById<Button>(R.id.Signout_btn_cancel).setOnClickListener {
                dialog.dismiss()
            }

            view.findViewById<Button>(R.id.Signout_btn_check).setOnClickListener {


                if (!myProfileUri) {
                    Toast.makeText(this, "사진을 업로드하지 않았어요 !!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                //닉네임 바인딩
                binding.SignUpConstNickName.helperText = validNIck()
                val validNick = binding.SignUpConstNickName.helperText == null

                if (validNick) {

                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("잠시만 기다려 주세요")
                    builder.setIcon(R.mipmap.ic_mbti_talk)

                    val v1 = layoutInflater.inflate(R.layout.progressbar, null)
                    builder.setView(v1)

                    builder.show()

                    // 유저가 입력한 회원가입 정보 가져오기
                    val SignupActivity_age = binding.SignUpEtxtAge.text.toString().toInt()
                    val SignupActivity_nickName = binding.SignUpEtxtNickName.text.toString()


                    if (selectedUri != null) {
                        Log.d("vec", "Selected Image Uri: $selectedUri")
                        uploadImage(selectedUri) {
                            if (it != null) {

                                Toast.makeText(
                                    this@SignUpActivity2,
                                    "이미지 업로드 완료",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                                val userId = firebaseAuth.currentUser?.uid
                                if (userId != null) {
                                    val user = UserData(
                                        "googleLogIn",
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

                                val intent = Intent(this, MbtiActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@SignUpActivity2,
                                    "이미지 업로드 실패",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@SignUpActivity2,
                            "이미지를 선택해주세요",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
            dialog.show()
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
                        this@SignUpActivity2,
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


    //닉네임 칸
    private fun NickFocusListener() {
        binding.SignUpEtxtNickName.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.SignUpConstNickName.helperText = validNIck()
            }
        }
    }

    //닉네임 양식체크
    private fun validNIck(): String? {
        val passwordText = binding.SignUpEtxtNickName.text.toString()
        if (passwordText.length > 13) {
            return "12자 이내의 닉네임을 입력하세요"
        }
        return null
    }


}
