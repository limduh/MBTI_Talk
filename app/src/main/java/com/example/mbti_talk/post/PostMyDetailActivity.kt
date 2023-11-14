package nb_.mbti_talk.post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import nb_.mbti_talk.DetailActivity
import nb_.mbti_talk.R
import nb_.mbti_talk.databinding.ActivityPostMyDetailBinding

class PostMyDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostMyDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostMyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


        }

        binding.PostDetailMyBackarrow.setOnClickListener {
            finish()
        }
    }
    private fun deletePost(postId: String) {
        val postsRef = Firebase.database.reference.child("posts")

        // 해당 게시물을 DB에서 삭제
        postsRef.child(postId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "게시물이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                finish() // 액티비티 종료
            }
            .addOnFailureListener {
                Toast.makeText(this, "게시물 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
    }
}