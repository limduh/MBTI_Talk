package nb_.mbti_talk.MyProfile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mbti_talk.FriendBlockActivity
import nb_.mbti_talk.LogInActivity
import nb_.mbti_talk.MBTI.MbtiActivity
import nb_.mbti_talk.MBTI.MbtiDialogChoice
import nb_.mbti_talk.MBTI.MbtiTestActivity
import nb_.mbti_talk.R
import nb_.mbti_talk.UserData
import nb_.mbti_talk.databinding.FragmentMyProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import nb_.mbti_talk.Chat.User
import nb_.mbti_talk.databinding.ActivityPostWriteBinding


class MyProfileFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var ProfileImg:AppCompatImageView

    val storage = Firebase.storage("gs://mbti-talk-f2a04.appspot.com")
    private lateinit var myUserData : UserData


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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        database = FirebaseDatabase.getInstance().getReference("Users")

        // profile_btn_MyLikeList 클릭 이벤트 추가
//        binding.profileBtnMyLikeList.setOnClickListener {
//            // 사용자가 좋아요한 글들을 보여주는 화면으로 이동
//            val intent = Intent(requireContext(), PostLikeActivity::class.java)
//            startActivity(intent)
//        }

        binding.ProfileMbtiBtn.setOnClickListener {
            showMbtiChoiceDialog()
        }

        //로그아웃
        binding.ProfileBtnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LogInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }

        //회원탈퇴
        binding.ProfileBtnMemberout.setOnClickListener {
            signoutDialog()
        }

        binding.ProfileImg.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (requireContext().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED)
                    (requireContext() as Activity).requestPermissions(permission33, 100)
                else {
                    galleryLauncher.launch("image/*")
                }
            } else {
                if (requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    requireContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {
                    (requireContext() as Activity).requestPermissions(permission, 100)
                } else {
                    galleryLauncher.launch("image/*")
                }

            }


        }


        binding.tvBlockFriend.setOnClickListener {
            val intent = Intent(requireContext(), FriendBlockActivity::class.java)
            startActivity(intent)
        }

        // Firebase 초기화
        firebaseAuth = FirebaseAuth.getInstance()

        // 현재 로그인한 사용자의 UID 가져오기
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val currentUserId = currentUser.uid

            // DB에서 현재 사용자의 데이터 가져오기
            val usersReference = FirebaseDatabase.getInstance().getReference("Users")
            usersReference.child(currentUserId).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userData = snapshot.getValue(UserData::class.java)
                        if (userData != null) {
                            myUserData = userData
                        }
                        if (userData != null) {
                            binding.ProfileEmail.text = "${userData.user_email}"
                            binding.ProfileNickname.text = "${userData.user_nickName}"
                            binding.ProfileAge.text = "${userData.user_age}"
                            binding.ProfileGender.text = "${userData.user_gender}"
                            binding.ProfileMbti.text = "${userData.user_mbti}"

                            // Firebase Storage 에서 프로필 이미지 가져오기
                            val storage = FirebaseStorage.getInstance()
                            val imgRef = storage.getReference("images/${userData.user_profile}")

                            // Glide 라이브러리를 사용하여 프로필 이미지 설정
                            Glide.with(requireContext())
                                .load(imgRef)
                                .centerCrop()
                                .error(android.R.drawable.stat_notify_error)
                                .into(binding.ProfileImg)
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // 데이터 가져오기 실패 시 처리
                }
            })
        }

        return view
    }


    //이미지 갤러리 불러오기
    val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//        binding.postImageSelect.tag = uri
        binding.ProfileImg.setImageURI(uri)
        if (uri != null) {
            uploadImage(uri){
                val userId = firebaseAuth.currentUser?.uid
                if (userId != null) {

                    myUserData.user_profile = it.toString()
                    database.child(userId).setValue(myUserData)
                }
            }
        }
    }

    fun makeFilePath(path: String, userId: String, uri: Uri): String {
        val mimeType = requireContext().contentResolver.getType(uri) ?: "/none" // MIME 타입 ex) images/jpeg
        val ext = mimeType.split("/")[1] // 확장자 ex) jpeg
        val timeSuffix = System.currentTimeMillis() // 시간값 ex) 1235421532
        val filename = "${path}/${userId}_${timeSuffix}.${ext}" // 완성!
        return filename
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

    private fun showMbtiChoiceDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.mbti_dialog_choice, null)
        val binding = MbtiDialogChoice(
            dialogView.findViewById(R.id.mbti_test_btn),
            dialogView.findViewById(R.id.mbti_activity_btn)
        )

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        binding.mbtiTestBtn.setOnClickListener {
            val intent = Intent(requireContext(), MbtiTestActivity::class.java)
            startActivity(intent)
            alertDialog.dismiss()
        }

        binding.mbtiActivityBtn.setOnClickListener {
            val intent = Intent(requireContext(), MbtiActivity::class.java)
            startActivity(intent)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun revokeAccess() {
        firebaseAuth.currentUser!!.delete()
    }

    private fun signoutDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(nb_.mbti_talk.R.layout.dialog_signout, null)
        val binding = SignoutDialogChoice(
            dialogView.findViewById(nb_.mbti_talk.R.id.Signout_btn_check),
            dialogView.findViewById(nb_.mbti_talk.R.id.Signout_btn_cancel)
        )

        val alertDialog = android.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        binding.SignoutBtnCheck.setOnClickListener {
            revokeAccess()
            val intent = Intent(requireContext(), LogInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            alertDialog.dismiss()

        }

        binding.SignoutBtnCancel.setOnClickListener {

            alertDialog.dismiss()
        }

        alertDialog.show()
    }




}