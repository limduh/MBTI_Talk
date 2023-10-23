package com.example.mbti_talk.post

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mbti_talk.PostData
import com.example.mbti_talk.databinding.ActivityPostListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class PostActivity : AppCompatActivity() {
    lateinit var binding: ActivityPostListBinding
    lateinit var postAdapter: PostAdapter
    private val postList = mutableListOf<PostData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postAdapter = PostAdapter(postList)
        // RecyclerView의 adapter에 ContentAdapter를 설정한다.
        binding.recyclerview.adapter = postAdapter
        // layoutManager 설정
        // LinearLayoutManager을 사용하여 수직으로 아이템을 배치한다.
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        // 글쓰기 버튼을 클릭 했을 경우 ContentWriteActivity로 이동한다.
        binding.contentWriteBtn.setOnClickListener {
            startActivity(Intent(this, PostWriteActivity::class.java))
        }
        // 데이터베이스에서 데이터 읽어오기
        getFBContentData()
    }
//    private fun saveImageToGallery(imageView: ImageView) {
//        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
//
//        val stream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//        val byteArray = stream.toByteArray()
//
//        try {
//            val contentValues = ContentValues().apply {
//                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//                put(MediaStore.Images.Media.IS_PENDING, 1)
//            }
//
//            val contentResolver = applicationContext.contentResolver
//            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//
//            uri?.let {
//                val fos = contentResolver.openOutputStream(uri)
//                fos?.write(byteArray)
//                fos?.close()
//
//                contentValues.clear()
//                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
//                contentResolver.update(uri, contentValues, null, null)
//
//                Toast.makeText(this, "이미지가 갤러리에 저장되었습니다.", Toast.LENGTH_SHORT).show()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Toast.makeText(this, "이미지 저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun getFBContentData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()

                for (data in snapshot.children) {
                    val key = data.key
                    val item = data.getValue(PostData::class.java)
                    Log.d("ContentListActivity", "item: ${item}")
                    // 리스트에 읽어 온 데이터를 넣어준다.
                    postList.add(item!!)
                }
                postList.reverse()
                // notifyDataSetChanged()호출하여 adapter에게 값이 변경 되었음을 알려준다.
                postAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        // addValueEventListener() 메서드로 DatabaseReference에 ValueEventListener를 추가한다.
//        FBRef.contentRef.addValueEventListener(postListener)
    }
}