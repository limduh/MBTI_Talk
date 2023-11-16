package nb_.mbti_talk.post

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import nb_.mbti_talk.R
import nb_.mbti_talk.databinding.ActivityPostItemBinding
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class PostAdapter(private var postList: List<PostData>, val myUid: String?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // onCreateViewHolder 메서드에서 뷰홀더를 생성합니다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ActivityPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    // onBindViewHolder 메서드에서 뷰홀더에 데이터를 바인딩합니다.
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = postList[position]
        if (holder is PostViewHolder) {
            holder.bind(item)
        }
    }

    // getItemCount 메서드에서 데이터의 개수를 반환합니다.
    override fun getItemCount(): Int {
        return postList.size
    }

    inner class PostViewHolder(val binding: ActivityPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                val context = itemView.context
                val postData = postList[position]

                if (postData.user_uid == myUid) {
                    val intent = Intent(context, PostMyDetailActivity::class.java)
                    // 여기에 데이터 전달 코드 추가
                    intent.putExtra("postId", postData.postId)
                    intent.putExtra("userId", postData.user_uid)

                    // ... (나머지 데이터 추가)

                    context.startActivity(intent)
                } else {
                    // 일반 게시물의 경우 PostDetailActivity로 이동
                    val intent = Intent(context, PostDetailActivity::class.java)
                    // 여기에 데이터 전달 코드 추가
                    intent.putExtra("title", postData.title)
                    intent.putExtra("content", postData.content)
                    intent.putExtra("image", postData.image)
                    intent.putExtra("user_age", postData.user_age)
                    intent.putExtra("user_nickName", postData.user_nickName)
                    intent.putExtra("user_profile", postData.user_profile)
                    intent.putExtra("user_mbti", postData.user_mbti)
                    intent.putExtra("user_gender", postData.user_gender)
                    intent.putExtra("time", postData.time)
                    intent.putExtra("userId", postData.user_uid)
                    // ... (나머지 데이터 추가)

                    context.startActivity(intent)
                }
            }
        }

        fun bind(postData: PostData) = binding.apply {
            Log.d("PostAdapter","jblee bind>>  $adapterPosition ")

            binding.PostItemTitle.text = postData.title
            binding.PostItemTime.text = postData.time
            binding.PostItemTxtUserNickname.text = postData.user_nickName
            val postId = postData.postId

            val storage = FirebaseStorage.getInstance()
            val reference = storage.getReference("images/${postData.image}")
            reference.downloadUrl.addOnSuccessListener {
                Log.d("bind", "image${postData.image}")
                Glide.with(binding.root)
                    .load(it)
                    .into(PostItemImgPost)
            }
            val profileReference = storage.getReference("images/${postData.user_profile}")
            profileReference.downloadUrl.addOnSuccessListener {
                Log.d("bind", "image${postData.user_profile}")
                Glide.with(binding.root)
                    .load(it)
                    .into(PostItemImgUserprofile)
            }

            val isCheck = getLikeState(postId, myUid!!) ;
            Log.d("PostAdapter","jblee bind>>  $adapterPosition  isCheck = $isCheck ")

            if(isCheck)
                binding.PostItemLike.setImageResource(R.drawable.ic_post_like_on)
            else
                binding.PostItemLike.setImageResource(R.drawable.ic_post_like_off)


            val postsRef = FirebaseData.database.getReference("posts")

            binding.PostItemLike.setOnClickListener {

                val isCheck = getLikeState(postId, myUid!!) ;

                if(isCheck){
                    postsRef.child(postId).child("likeByUser").child(myUid!!).setValue(false)
                    Toast.makeText(itemView.context, "좋아요를 취소했습니다.", Toast.LENGTH_SHORT).show()
                    postList.get(adapterPosition).likeByUser.set(myUid,false)

                }else {
                    postsRef.child(postId).child("likeByUser").child(myUid!!).setValue(true)
                    Toast.makeText(itemView.context, "좋아요를 눌렀습니다.", Toast.LENGTH_SHORT).show()
                    postList.get(adapterPosition).likeByUser.set(myUid,true)
                }
                Log.d("PostAdapter","jblee $adapterPosition / myUid = $myUid / value = ${!isCheck} ")

                notifyItemChanged(adapterPosition)
            }
        }
    }

    fun updateList(newList: List<PostData>) {
        postList = newList
        notifyDataSetChanged()
    }
    fun getLikedPosts(): List<PostData> {
        return postList.filter {it.likeByUser.get(myUid)==true }
    }

    private fun getLikeState(postId:String,  uId:String): Boolean{

        var res = false

        val post = postList.filter { it.postId==postId && it.likeByUser.get(uId)==true }

        if(post.isNotEmpty())
            res = true

        return res


    }
}
