package nb_.mbti_talk.post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import nb_.mbti_talk.DetailActivity
import nb_.mbti_talk.databinding.ActivityPostDetailBinding
import com.google.firebase.storage.FirebaseStorage

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val image = intent.getStringExtra("image")
        val nickname = intent.getStringExtra("user_nickName")
        val profile = intent.getStringExtra("user_profile")
        val time = intent.getStringExtra("time")

        binding.PostDetailTitle.text = title
        binding.PostDetailEtContent.text = content
        binding.PostDetailUserName.text = nickname
        binding.PostDetailTime.text = time

        val storage = FirebaseStorage.getInstance()
        val reference = storage.reference.child("images/$image")
        reference.downloadUrl.addOnSuccessListener {
            Glide.with(this)
                .load(it)
                .into(binding.PostDetailImage)
        }
        val profileReference = storage.reference.child("images/$profile")
        profileReference.downloadUrl.addOnSuccessListener {
            Glide.with(this)
                .load(it)
                .into(binding.PostDetailUserpicture)
        }
        binding.PostDetailUserpicture.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)

            startActivity(intent)
        }


        binding.PostDetailBackarrow.setOnClickListener {
            finish()
        }
    }
}