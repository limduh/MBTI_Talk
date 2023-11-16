package nb_.mbti_talk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import nb_.mbti_talk.Chat.ChatRoom
import nb_.mbti_talk.Chat.ChatRoomActivity
import nb_.mbti_talk.databinding.ActivityDetailBinding
import nb_.mbti_talk.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class DetailActivity : AppCompatActivity() {


    private lateinit var friendBlockDB: DatabaseReference

    private val oponentBlockList: MutableList<UserData> = mutableListOf()

    // DetailActivity 클래스의 멤버 변수들을 선언
    lateinit var binding: ActivityDetailBinding // 뷰바인딩 초기화
    lateinit var detailDB: DatabaseReference // RDB 와 연동하기 위한 레퍼런스를 초기화

    // TextView 초기화
    private lateinit var nameTextView: AppCompatTextView
    private lateinit var ageTextView: AppCompatTextView
    private lateinit var genderTextView: AppCompatTextView
    private lateinit var mbtiTextView: AppCompatTextView

    // ImageView 초기화
    private lateinit var profileImageView: AppCompatImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰 바인딩 초기화, 해당 바인딩을 현재 액티비티 레이아웃으로 설정
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TextView 초기화
        nameTextView = binding.DetailTxtNickname
        ageTextView = binding.DetailTxtAge
        genderTextView = binding.DetailTxtGender
        mbtiTextView = binding.DetailTxtMbti
        // ImageView 초기화
        profileImageView = binding.DetailIvProfile

        // "현재 사용자"의 UID 가져옴
        val myId = Utils.getMyUid(this)

        // MainActivity로부터 전달받은 intent를 통해 "선택한 사용자"의 UID를 가져옴
        val userID = intent.getStringExtra("userId")

        // 친구목록, 친구찾기 탭에서 viewtype을 받음
        val viewType = intent.getStringExtra("viewtype")
        Log.d("DetailActivity", "viewType = $viewType")
        viewType?.let {
            // 키값이 list 일 경우에만, 친구추가 사라짐.
            if (it == "list") {
                binding.DetailBtnFriendAdd.visibility = View.GONE
                binding.DetailTxtFriendAdd.visibility = View.GONE
            }
        }

        Log.d("DetailActivity", "My userID = $myId")
        Log.d("DetailActivity", "Selected userID = $userID")


        detailDB = Firebase.database.reference.child("Users") // RDB 초기화하고 "Users" 레퍼런스 가져오기

        // RDB 에서 사용자 데이터 가져오기 ( 주요 목적은 사용자 ID(uid)를 기반으로 DB에서 해당 user 정보를 찾아내고, 화면 표시 위해 userData에 저장. 한 번에 한 사용자의 정보만 가져옴.)

        detailDB.addListenerForSingleValueEvent(object :
            ValueEventListener { // RDB에서 데이터를 읽어오기 위한 리스너를 설정. 데이터의 한 번 읽기 작업을 수행
            override fun onDataChange(snapshot: DataSnapshot) { // 데이터를 성공적으로 읽어왔을 때 호출.snapshot은 데이터베이스에서 가져온 정보(=uid)
                Log.d(
                    "DetailActivity",
                    "snapshot.exists() = ${snapshot.exists()}"
                ) // snapshot.exists()를 통해 스냅샷이 데이터를 포함하는지 여부를 확인

                if (snapshot.exists()) { // 데이터 스냅샷 존재 확인. 스냅샷이 데이터 포함 시 이 블록 안으로 진입
                    lateinit var userData: DataSnapshot

                    // 선택한 유저 데이터 찾기
                    if (userID != null) {
                        userData =
                            snapshot.child(userID) // userID가 null이 아닌 경우, snapshot.child(userID)를 사용하여 snapshot에서 해당 사용자 ID에 해당하는 데이터 스냅샷을 가져옴. 이는 특정 사용자의 데이터를 나타내며, userData 변수에 저장

                        // 데이터에서 이름, 나이, 성별, MBTI 정보 가져오기 (RDB 에서 특정 유저 정보를 가져와 변수에 저장하는 부분. DB의 트리 구조와 각 데이터 유형에 맞게 데이터 뽑아옴)
                        val name = userData.child("user_nickName")
                            .getValue<String?>() // userData라는 DataSnapshot에서 "user_nickName"이라는 자식 경로에 있는 값을 가져옴.
                        val age = userData.child("user_age")
                            .getValue<Int?>() // 여기서는 user 데이터 아래에 nickname, age, gender, mbti 라는 자식 경로로 데이터가 저장되어있음
                        val gender = userData.child("user_gender").getValue<String?>()
                        val mbti = userData.child("user_mbti").getValue<String?>()

                        // 가져온 데이터를 TextView에 설정
                        nameTextView.text = name
                        ageTextView.text = age.toString()
                        genderTextView.text = gender
                        mbtiTextView.text = mbti

                        // Firebase Storage 에서 프로필 이미지 가져오기
                        val storage = FirebaseStorage.getInstance()
                        val imgRef = storage.getReference(
                            "images/${
                                userData.child("user_profile").getValue<String?>()
                            }"
                        )

                        // Glide 라이브러리를 사용하여 imgRef 에 있는 이미지를 user_profile 에 표시
                        Glide.with(binding.root.context)
                            .load(imgRef)
                            .centerCrop()
                            .error(android.R.drawable.stat_notify_error)
                            .into(profileImageView)

                    } else {
                        // 사용자 정보를 가져오지 못한 경우
                        Toast.makeText(
                            this@DetailActivity,
                            "사용자 정보를 가져오는데 실패했습니다.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // DB 가져오는 과정에서 오류 발생 시, 오류 메시지 출력
                Toast.makeText(this@DetailActivity, "데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        val btnFriendAdd = binding.DetailBtnFriendAdd
        val txtFriendAdd = binding.DetailTxtFriendAdd

        btnFriendAdd.setOnClickListener {
            // RDB 의 "Friends" 레퍼런스에 사용자 uid 추가
            if (userID != null) {
                val friendsRef = Firebase.database.reference.child("Friends").child(myId.toString())
                friendsRef.child(userID).setValue(true)
            }
            // 클릭 이벤트 처리
            // 버튼과 텍스트 뷰를 보이지 않게 설정
            btnFriendAdd.visibility = View.GONE
            txtFriendAdd.visibility = View.GONE
            Toast.makeText(this@DetailActivity, "친구 추가가 완료되었습니다.", Toast.LENGTH_SHORT).show()
        }
        // 뒤로가기 누르면 현 액티비티 종료
        binding.DetailBackArrow.setOnClickListener {
            finish()
        }
        //채팅하기 버튼 누름 요기좀 바뀜
        binding.DetailBtnChat.setOnClickListener {
            detailDB.addListenerForSingleValueEvent(object :
                ValueEventListener { // RDB에서 데이터를 읽어오기 위한 리스너를 설정. 데이터의 한 번 읽기 작업을 수행
                override fun onDataChange(snapshot: DataSnapshot) { // 데이터를 성공적으로 읽어왔을 때 호출.snapshot은 데이터베이스에서 가져온 정보(=uid)
                    if (snapshot.exists()) { // 데이터 스냅샷 존재 확인. 스냅샷이 데이터 포함 시 이 블록 안으로 진입
                        lateinit var userData: DataSnapshot
                        // 선택한 유저 데이터 찾기
                        if (userID != null) {
                            userData =
                                snapshot.child(userID)
                            val name = userData.child("user_nickName").getValue<String?>()
                            val useremail = userData.child("user_email").getValue<String?>()
                            val user_age=userData.child("user_age").getValue<Int?>()
                            val user_gender = userData.child("user_gender").getValue<String?>()
                            val user_mbti = userData.child("user_mbti").getValue<String?>()
                            val user_pofile=userData.child("user_profile").getValue<String?>()
                            if(name !=null&& useremail !=null&& user_age!=null&& user_gender!=null&& user_mbti!=null&&user_pofile!=null ){
                                val opponent = UserData(useremail, user_age, name, userID, user_gender, user_mbti,user_pofile) //채팅할 상대방 정보
                            var database =
                                FirebaseDatabase.getInstance()
                                    .getReference("ChatRoom")    //넣을 database reference 세팅
                            var chatRoom = ChatRoom(
                                //추가할 채팅방 정보 세팅
                                mapOf(myId!! to true, userID!! to true), null
                            )

                            val chatRoomKey = if (myId!! < userID!!) {
                                "${myId}-${userID}"
                            } else {
                                "${userID}-${myId}"
                            }
                            var myUid = FirebaseAuth.getInstance().uid!!//내 Uid
                            database.child("chatRooms").child(chatRoomKey).child("users")
                                .child(myUid)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val isUserInChatRoom =
                                            snapshot.getValue(Boolean::class.java)
                                        if (isUserInChatRoom == true) {
                                            // 사용자가 해당 채팅방에 있는 경우, 해당 채팅방으로 이동
                                            val intent = Intent(this@DetailActivity, ChatRoomActivity::class.java)
                                            intent.putExtra("ChatRoomKey", chatRoomKey)
                                            intent.putExtra("ChatRoom", chatRoom)
                                            intent.putExtra("Opponent", opponent)
                                            startActivity(intent)
                                        } else {                                                                         //채팅방이 없는경우
                                            database.child("chatRooms").child(chatRoomKey)
                                                .setValue(chatRoom)
                                                .addOnSuccessListener {// 채팅방 새로 생성 후 이동
                                                    goToChatRoom(chatRoom, chatRoomKey, opponent)
                                                }
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                        // 처리 중 오류가 발생한 경우
                                    }
                                })

                        } }else {
                            // 사용자 정보를 가져오지 못한 경우
                            Toast.makeText(this@DetailActivity, "사용자 정보를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // DB 가져오는 과정에서 오류 발생 시, 오류 메시지 출력
                    Toast.makeText(this@DetailActivity, "데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }


        binding.llFriendBlock.setOnClickListener {

            if (userID != null) { // 선택한 유저 UID 가 null 이 아닌 경우

                // RDB 의 "Friends_block" 레퍼런스에 사용자 uid 추가
                val friendsRef = Firebase.database.reference.child("Friends_block").child(myId.toString())
                friendsRef.child(userID).setValue(true)

                // 채팅방 삭제 함수
                fun findChatRoomWithBlockedFriend(myUid: String, blockedFriendUid: String) {
                    // "ChatRoom/chatRooms" 노드에서 유저와 차단할 유저 포함된 채팅방 목록 조회
                    val chatRoomsRef = FirebaseDatabase.getInstance().getReference("ChatRoom").child("chatRooms")

                    chatRoomsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            // 데이터를 성공적으로 가져온 경우
                            for (chatRoomSnapshot in snapshot.children) {
                                // 각 채팅방 대해 반복 처리
                                val chatRoom = chatRoomSnapshot.getValue(ChatRoom::class.java)
                                val chatRoomUsers = chatRoom?.users?.keys ?: continue

                                // 현재 사용자와 차단할 친구 모두 포함된 채팅방 확인
                                if (chatRoomUsers.contains(myUid) && chatRoomUsers.contains(blockedFriendUid)) {
                                    // 조건 만족하는 채팅방 삭제
                                    chatRoomSnapshot.ref.removeValue()
                                    Log.d("ChatRoom", "#byurin > 현재 사용자와 차단할 친구가 모두 포함된 채팅방을 찾음 : ${chatRoomSnapshot.key}.")
                                }
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            // 처리 중 오류 발생한 경우
                            Log.d("ChatRoom", "#byurin > 채팅방 삭제 중 오류 발생 : ${databaseError.message}")
                        }
                    })
                }

                // 채팅방 삭제 함수 호출
                findChatRoomWithBlockedFriend(myId.toString(), userID)


            }
            // 클릭 이벤트 처리
            // Intent 사용하여 MainFriendActivity 로 이동
            Toast.makeText(this@DetailActivity, "친구 차단이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            finish()

        }
        // Firebase DB 참조
        friendBlockDB = Firebase.database.reference.child("Friends_block")


        // selectedUserId는 intent.getStringExtra("userId")를 통해 DetailActivity로 전달된 선택된 사용자의 UID를 받아오기 위해 사용됩니다.
        //이는 DetailActivity가 시작될 때 전달되는 인텐트에서 추출된 데이터로, 다른 사용자의 상세 정보를 표시하기 위해 필요합니다.

        // 현재 사용자의 UID = myId, 선택한 사용자의 UID = userID

        // A가 B를 차단했는지 확인
        userID?.let { selectedUserId -> // "userId" 를 통해 DetailActivity 로 전달된 userId UID 받기 위해 사용
            // 선택된 사용자('selectedUserId') 가 현재 사용자('myId') 를 차단했는지 확인
            friendBlockDB.child(selectedUserId).child(myId.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        // 데이터를 성공적으로 가져온 경우
                        if (snapshot.exists()) { // 해당 경로에 데이터 존재하는지 확인하여 차단 여부 판단
                            // A가 B를 차단한 경우 차단 안내 토스트 메시지 표시
                            Toast.makeText(
                                this@DetailActivity,
                                "해당 사용자가 당신을 차단했습니다.\n채팅신청이 불가능합니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            // 채팅하기 UI 숨기기
                            hideFriendInteractionUI()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        // 처리 중 오류 발생한 경우
                        Toast.makeText(
                            this@DetailActivity,
                            "데이터 처리에 실패했습니다.\n다시 시도해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    private fun hideFriendInteractionUI() {
        // 채팅하기 버튼 숨기기
        binding.DetailBtnChat.visibility = View.GONE
        binding.DetailTxtChat.visibility = View.GONE
    }

    fun goToChatRoom(chatRoom: ChatRoom, chatRoomKey: String, opponent: UserData) {
        val intent = Intent(this, ChatRoomActivity::class.java)
        intent.putExtra("ChatRoom", chatRoom)
        intent.putExtra("Opponent", opponent)
        intent.putExtra("ChatRoomKey", chatRoomKey)
        startActivity(intent)
        finish()
    }


}

