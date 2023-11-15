package nb_.mbti_talk.post

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import nb_.mbti_talk.UserData
import nb_.mbti_talk.databinding.ActivityPostWriteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PostWriteActivity : AppCompatActivity() {
    private var postId_edit: String? = null
    private var imageSelected = false


    lateinit var binding: ActivityPostWriteBinding
    private lateinit var firebaseAuth: FirebaseAuth
    val storage = Firebase.storage("gs://mbti-talk-f2a04.appspot.com")

    var isEditMode = false;
    var isEditModeForImage =false
    var SelectedImage = ""

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permission33 = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_MEDIA_VIDEO,
    )

    private val permission = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.llLoadingPostWrite.setOnClickListener{

        }
        postId_edit = intent.getStringExtra("postId")
        if (postId_edit != null) {
            isEditMode = true
        }
        postId_edit?.let {
            loadPostData(it)
        }

        val btnBack = binding.postbackarrow
        btnBack.setOnClickListener {
            finish()
        }
        firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().reference

        binding.postImageSelect.setOnClickListener {

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
                    requestPermissions(permission, 100)
                } else {
                    galleryLauncher.launch("image/*")
                }

            }
        }

        binding.postSave.setOnClickListener {
            binding.llLoadingPostWrite.visibility = View.VISIBLE
            val title = binding.postTitle.text.toString()
            val content = binding.postEtContent.text.toString()
            val time = getTime2()
            val uri = SelectedImage

            Log.d("Storage", "jb# postSave uri -> ${uri.toString()}")

            if (uri.isNotEmpty()) {

                if (isEditMode && !isEditModeForImage) {
                    val user = PostData(title, content, time, uri.toString())
                    addItem(user)
                    Toast.makeText(this@PostWriteActivity, "게시글 입력 완료", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                    binding.llLoadingPostWrite.visibility = View.GONE
                } else {
                    uploadImage(uri.toUri()) {
                        Log.d("Storage", "jb# postSave uploadImage uri -> ${it.toString()}")

                        if (it != null) {
                            val user = PostData(title, content, time, it)
                            addItem(user)
                            Toast.makeText(this@PostWriteActivity, "게시글 입력 완료", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        } else {
                            Toast.makeText(this@PostWriteActivity, "이미지 업로드 실패", Toast.LENGTH_SHORT)
                                .show()
                        }
                        binding.llLoadingPostWrite.visibility = View.GONE
                    }
                }
            } else {
                Toast.makeText(this@PostWriteActivity, "이미지를 선택해주세요", Toast.LENGTH_SHORT).show()
                binding.llLoadingPostWrite.visibility = View.GONE
            }

        }

        val editText = binding.postEtContent

        val filter = object : InputFilter {
            override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
                val inputText = dest.toString() + source.toString()
                if (inputText.length <= 500) {
                    return null
                } else {
                    showToast("500자를 초과할 수 없습니다.")
                    return source?.subSequence(0, 500 - dest.toString().length)
                }
            }
        }

        editText.filters = arrayOf(filter)
    }

    private fun loadPostData(postId: String) {
        val postsRef = Firebase.database.reference.child("posts").child(postId)

        postsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("PostWriteActivity", "Data Change Event Occurred")

                if (snapshot.exists()) {
                    val postData = snapshot.getValue(PostData::class.java)

                    Log.d("PostWriteActivity", "Loaded Post Data: $postData")

                    // 가져온 데이터를 화면에 출력
                    postData?.let {
                        binding.postTitle.setText(it.title)
                        binding.postEtContent.setText(it.content)
                        SelectedImage = it.image

                        Log.d("Storage", "jb# postsRef uri -> ${it.image}")

                        // 이미지 출력을 위한 코드
                        val storage = FirebaseStorage.getInstance()
                        val reference = storage.reference.child("images/${postData.image}")
                        reference.downloadUrl.addOnSuccessListener { uri ->

                            Log.d("image", "images/${postData.image}")
                            Glide.with(this@PostWriteActivity)
                                .load(uri)
                                .into(binding.postImage)
                        }

                    }
                } else {
                    Toast.makeText(
                        this@PostWriteActivity,
                        "게시물이 존재하지 않습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("PostWriteActivity", "Data Change Event Canceled")
                Toast.makeText(
                    this@PostWriteActivity,
                    "데이터를 가져오는데 실패했습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                100 -> {
                    Toast.makeText(
                        this@PostWriteActivity,
                        "권한 허용됨",
                        Toast.LENGTH_SHORT
                    ).show()
                    galleryLauncher.launch("image/*")

                }
            }
        }
    }

    fun getTime(): String {
        val currentDateTime = Calendar.getInstance().time
        val dateFormat =
            SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(currentDateTime)

        return dateFormat
    }

    fun getTime2(): String {
        val currentDateTime = Calendar.getInstance().time
        val dateFormat =
            SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDateTime)

        return dateFormat
    }

    fun addItem(user: PostData): String {
        val userUid = FirebaseAuth.getInstance().currentUser?.uid
        user.user_uid = userUid ?: ""

        val userRef = FirebaseData.database.getReference("Users").child(userUid!!)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserData::class.java)
                if (userData != null) {
                    val user_nickName = userData.user_nickName
                    val user_profile = userData.user_profile
                    val user_mbti = userData.user_mbti
                    val user_gender = userData.user_gender
                    val user_age = userData.user_age


                    val postId = userUid!! + getTime()
                    var new_postId = postId

                    if (isEditMode) {
                        new_postId = postId_edit.toString()
                    }
                    if (user_nickName != null && user_profile != null) {
                        user.apply {
                            this.user_nickName = user_nickName
                            this.user_profile = user_profile
                            this.user_mbti = user_mbti
                            this.user_gender = user_gender
                            this.user_age = user_age
                            this.postId = new_postId
                        }
                        FirebaseData.mydata.child(new_postId).setValue(user)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PostWriteActivity, "닉네임과 프로필 사진 받기 실패", Toast.LENGTH_SHORT)
                    .show()
            }
        })
        return userUid

    }

    //이미지 갤러리 불러오기
    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        Log.d("Storage", "jb# galleryLauncher uri -> ${uri}")
        if(isEditMode)
        isEditModeForImage = true


        SelectedImage = uri.toString()
        binding.postImage.setImageURI(uri)
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
}

class PostData {
    var postId: String = "" // 게시물 고유 식별자
    var user_uid: String = "" // 게시물 고유 식별자
    var title: String = ""
    var content: String = ""
    var time: String = ""
    var image: String = ""
    var user_nickName: String = ""
    var user_profile: String = ""
    var user_mbti: String = ""
    var user_gender: String = ""
    var user_age: Int = 0
    var likeByUser: MutableMap<String, Boolean> = HashMap()

    constructor()
    constructor(title: String, content: String, time: String, image: String) {
        this.title = title
        this.content = content
        this.time = time
        this.image = image
    }
}
