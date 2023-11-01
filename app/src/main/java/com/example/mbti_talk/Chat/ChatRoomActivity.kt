package com.example.mbti_talk.Chat


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mbti_talk.UserData
import com.example.mbti_talk.databinding.ActivityChatRoomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

class ChatRoomActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatRoomBinding
    lateinit var btn_exit: ImageView
    lateinit var btn_submit: Button
    lateinit var txt_title: TextView
    lateinit var edt_message: EditText
    lateinit var firebaseDatabase: DatabaseReference
    lateinit var recycler_talks: RecyclerView
    lateinit var chatRoom: ChatRoom
    lateinit var opponentUser: UserData
    lateinit var chatRoomKey: String
    lateinit var myUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myUid = FirebaseAuth.getInstance().currentUser?.uid!!              //현재 로그인한 유저 id
        firebaseDatabase = FirebaseDatabase.getInstance().reference!!

        chatRoom = intent.getSerializableExtra("ChatRoom") as ChatRoom      //채팅방 정보
        chatRoomKey = intent.getStringExtra("ChatRoomKey")!! //채팅방 키
        opponentUser = intent.getParcelableExtra("Opponent")!! //상대방 유저 정보


        initializeView()
        initializeListener()
        setupChatRooms()

    }


    fun initializeView() {    //뷰 초기화
        edt_message = binding.edtMessage
        recycler_talks = binding.recyclerMessages
        btn_submit = binding.btnSubmit
        btn_exit=binding.btnExit
        txt_title = binding.txtTItle
        txt_title.text = opponentUser!!.user_nickName ?: ""
    }

    fun initializeListener() {   //버튼 클릭 시 리스너 초기화
        btn_exit.setOnClickListener()
        {
          finish()
        }
        btn_submit.setOnClickListener()
        {
            putMessage()
        }
    }

    fun setupChatRooms() {              //채팅방 목록 초기화 및 표시
        if (chatRoomKey.isNullOrBlank())
            setupChatRoomKey()
        else
            setupRecycler()
    }

    fun setupChatRoomKey() {            //chatRoomKey 없을 경우 초기화 후 목록 초기화
        FirebaseDatabase.getInstance().getReference("ChatRoom")
            .child("chatRooms").orderByChild("${chatRoomKey}/${myUid}").equalTo(true)    //나의 Uid가 포함된 목록이 있는지 확인
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        chatRoomKey = data.key!!          //chatRoomKey 초기화
                        setupRecycler()                  //목록 업데이트
                        break
                    }
                }
            })
    }

    fun putMessage() {       //메시지 전송
        var message = Message(myUid, getDateTimeString(), edt_message.text.toString())    //메시지 정보 초기화

        FirebaseDatabase.getInstance().getReference("ChatRoom").child("chatRooms")
            .child(chatRoomKey).child("messages")                   //현재 채팅방에 메시지 추가
            .push().setValue(message).addOnSuccessListener {
                edt_message.text.clear()
            }
    }

    fun getDateTimeString(): String {          //메시지 보낸 시각 정보 반환
        try {
            var localDateTime = LocalDateTime.now()
            localDateTime.atZone(TimeZone.getDefault().toZoneId())
            var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
            return localDateTime.format(dateTimeFormatter).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("getTimeError")
        }
    }

    fun setupRecycler() {            //목록 초기화 및 업데이트
        recycler_talks.layoutManager = LinearLayoutManager(this)
        recycler_talks.adapter = RecyclerMessagesAdapter(this, chatRoomKey, opponentUser.user_uid)
    }
}