package com.example.mbti_talk.post

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mbti_talk.databinding.ActivityPostWriteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PostWriteActivity : AppCompatActivity() {


    lateinit var binding: ActivityPostWriteBinding
    private lateinit var firebaseAuth: FirebaseAuth
    val storage = Firebase.storage("gs://mbti-talk-f2a04.appspot.com")
    private var prevX = 0f
    private var prevY = 0f


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
        val btnBack = binding.postbackarrow
        btnBack.setOnClickListener {
            finish()
        }
        firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().reference

        binding.appCompatImageView5.setOnClickListener {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        binding.postSave.setOnClickListener {
            val title = binding.postTitle.text.toString()
            val content = binding.postEtContent.text.toString()
            val time = getTime()
            val uri = binding.appCompatImageView5.tag as? Uri
            if (uri != null) {
                Log.d("vec", "Selected Image Uri: $uri")
                uploadImage(uri) {
                    if (it != null) {
                        val user = PostData(title, content, time, it)
                        val id = addItem(user)
                        Toast.makeText(this@PostWriteActivity, "게시글 입력 완료", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this, PostActivity::class.java)
                        intent.putExtra("POSTLIST", id)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@PostWriteActivity, "이미지 업로드 실패", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(this@PostWriteActivity, "이미지를 선택해주세요", Toast.LENGTH_SHORT).show()
            }
        }
        binding.postImage.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 이미지 터치 시 이동을 시작합니다.
                    prevX = event.rawX
                    prevY = event.rawY
                }

                MotionEvent.ACTION_MOVE -> {
                    // 이미지를 이동합니다.
                    val deltaX = event.rawX - prevX
                    val deltaY = event.rawY - prevY
                    val newX = v.x + deltaX
                    val newY = v.y + deltaY

                    // 이미지가 화면을 벗어나지 않도록 제한합니다.
                    val maxX = binding.postConstContent.width - v.width
                    val maxY = binding.postConstContent.height - v.height
                    v.x = Math.min(maxX.toFloat(), Math.max(0f, newX))
                    v.y = Math.min(maxY.toFloat(), Math.max(0f, newY))

                    prevX = event.rawX
                    prevY = event.rawY
                }
            }
            true
        }
    }
    fun getTime(): String {
        val currentDateTime = Calendar.getInstance().time
        val dateFormat =
            SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA).format(currentDateTime)

        return dateFormat
    }
    fun addItem(user: PostData): String {
        val id = FirebaseData.mydata.push().key!!
        user.uid = id
        FirebaseData.mydata.child(id).setValue(user)
        return id

    }

    //권한 요청하기
    val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                galleryLauncher.launch("image/*")
            } else {
                Toast.makeText(baseContext, "외부 저장소 읽기 권한을 승인해야 사용할 수 있습니다.", Toast.LENGTH_LONG)
                    .show()
            }
        }

    //이미지 갤러리 불러오기
    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        binding.appCompatImageView5.tag = uri
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
    var uid: String = "" // 게시물 고유 식별자
    var title: String = ""
    var content: String = ""
    var time: String = ""
    var image: String = ""

    constructor()
    constructor(title: String, content: String, time: String, image: String) {

        this.title = title
        this.content = content
        this.time = time
        this.image = image
    }
}