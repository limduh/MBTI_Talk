package com.example.mbti_talk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(
    val items: MutableList<UserData>
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    // View Holder를 생성하고 View를 붙여주는 메서드
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_post_item, parent, false)
        return ViewHolder(v)
    }

    // 생성된 View Holder에 데이터를 바인딩 해주는 메서드
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTtems(items[position])
    }

    // 데이터의 개수를 반환하는 메서드
    override fun getItemCount(): Int {
        return items.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        init {
//            itemView.setOnLongClickListener {
//                onItemLongClick(itemView.findViewById(R.id.imageArea))
//                true
//            }
//        }

        fun bindTtems(items: UserData) {
            val title = itemView.findViewById<TextView>(R.id.titleArea)
            val content = itemView.findViewById<TextView>(R.id.contentArea)
            val time = itemView.findViewById<TextView>(R.id.timeArea)
//            val imageUrl = itemView.findViewById<ImageView>(R.id.imageArea)

//            title.text = items.title
//            content.text = items.content
//            time.text = items.time

        }
    }
}