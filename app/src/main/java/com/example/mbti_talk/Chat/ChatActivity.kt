package com.example.mbti_talk.Chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mbti_talk.R
import com.example.mbti_talk.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    private lateinit var receiverName:String
    private lateinit var receiverUid:String
    private lateinit var  binding: ActivityChatBinding
    lateinit var mAuth: FirebaseAuth
    lateinit var mDB: DatabaseReference
    private lateinit var receiverRoom:String //받는 대화방
    private lateinit var senderRoom:String  //보낸 대화방
    //메세지 담을 리스트sss
    private lateinit var messageList:ArrayList<Message>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        messageList=ArrayList()
        val messageAdapter:MessageAdapter= MessageAdapter(this,messageList)
        binding.chatRecyclerView.layoutManager= LinearLayoutManager(this)
        binding.chatRecyclerView.adapter=messageAdapter

        //넘어온 데이터 변수에 담기
        receiverName=intent.getStringExtra("name").toString()
        receiverUid=intent.getStringExtra("uId").toString()

        mAuth= FirebaseAuth.getInstance()
        mDB= FirebaseDatabase.getInstance().reference

        //접속자Uid
        val senderUid=mAuth.currentUser?.uid!!

        //보낸이방
        senderRoom= receiverUid +"_"+ senderUid

        //받는이방
        receiverRoom= senderUid +"_"+ receiverUid

        //액션바에 상대방 이름 보여주기
        supportActionBar?.title=receiverName

        //메세지 전송 버튼 이벤트
        binding.sendBtn.setOnClickListener{
            val message=binding.msgEdit.text.toString()
            val messageObject = Message(message,senderUid)
            //데이터 저장 보낸message
            mDB.child("chat").child(senderRoom).child("message").push().setValue(messageObject).addOnCompleteListener() {
                //데이터저장 받은 message
                mDB.child("chat").child(receiverRoom).child("message").push()
                    .setValue(messageObject)
            }
            //입력부분 초기화
            binding.msgEdit.setText("")
        }
        //메세지 가져오기 DB객체활용
        mDB.child("chat").child(senderRoom).child("message").addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for(postSnapshot in snapshot.children){
                    val message =postSnapshot.getValue(Message::class.java)
                    messageList.add(message!!)
                }
                //적용
                messageAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}