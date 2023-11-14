package nb_.mbti_talk.post

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import nb_.mbti_talk.DetailActivity
import nb_.mbti_talk.R
import nb_.mbti_talk.databinding.ActivityPostMyDetailBinding

class PostMyDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostMyDetailBinding
    private lateinit var postDB: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostMyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postDB = Firebase.database.reference.child("posts")

        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val image = intent.getStringExtra("image")
        val nickname = intent.getStringExtra("user_nickName")
        val profile = intent.getStringExtra("user_profile")
        val time = intent.getStringExtra("time")
        val userId = intent.getStringExtra("userId")
        val postId = intent.getStringExtra("postId")


        binding.PostDetailMyTitle.text = title
        binding.PostDetailMyEtContent.text = content
        binding.PostDetailMyUserName.text = nickname
        binding.PostDetailMyTime.text = time

        val storage = FirebaseStorage.getInstance()
        val reference = storage.reference.child("images/$image")
        reference.downloadUrl.addOnSuccessListener {
            Glide.with(this)
                .load(it)
                .into(binding.PostDetailMyImage)
        }
        val profileReference = storage.reference.child("images/$profile")
        profileReference.downloadUrl.addOnSuccessListener {
            Glide.with(this)
                .load(it)
                .into(binding.PostDetailMyUserpicture)
        }
        binding.PostDetailMyUserpicture.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("userId", userId) // 실제 사용자 ID로 대체
            startActivity(intent)
        }
        binding.PostDetailMyModify.setOnClickListener {
            val intent = Intent(this, PostWriteActivity::class.java)
            intent.putExtra("postId", postId) // 수정할 게시물의 ID를 전달
            startActivity(intent)


        }
        binding.PostDetailMyDelete.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_post_detail) // dialog_post_detail.xml의 레이아웃을 사용

            val btnCancel = dialog.findViewById<Button>(R.id.postdelete_btn_cancel)
            val btnCheck = dialog.findViewById<Button>(R.id.postdelete_btn_check)

            btnCancel.setOnClickListener {
                dialog.dismiss() // 다이얼로그를 닫습니다.
            }
            btnCheck.setOnClickListener {

                // 해당 게시물의 ID를 사용하여 Firebase Database에서 삭제
                postId?.let { id ->
                    postDB.child(id).removeValue().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            deleteImage(id)
                            // 삭제 성공 시 다이얼로그 닫고 액티비티 종료
                            Toast.makeText(this, "게시물이 삭제 되었습니다.",Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                            finish()
                        } else {
                            Toast.makeText(this,"삭제 중 오류가 발생했습니다.",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            dialog.show() // 다이얼로그를 표시합니다.
        }

        binding.PostDetailMyBackarrow.setOnClickListener {
            finish()
        }
    }
    fun deleteImage(image: String) {
        val storageReference = Firebase.storage.reference
        val imageRef = storageReference.child("images/$image") // 이미지 경로 지정 (파일 형식에 따라 확장자 변경 가능)

        imageRef.delete().addOnSuccessListener {
            // 이미지 삭제 성공 시
            Log.d("PostWriteActivity", "Image deleted successfully")
        }.addOnFailureListener { e ->
            // 이미지 삭제 실패 시
            Log.e("PostWriteActivity", "Failed to delete image: $e")
        }
    }
}