package com.example.mbti_talk.MyProfile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mbti_talk.LogInActivity
import com.example.mbti_talk.MBTI.MbtiActivity
import com.example.mbti_talk.MBTI.MbtiDialogChoice
import com.example.mbti_talk.MBTI.MbtiTestActivity
import com.example.mbti_talk.R
import com.example.mbti_talk.UserData
import com.example.mbti_talk.databinding.FragmentMyProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage


class MyProfileFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var ProfileImg:AppCompatImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        val view = binding.root

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
            startActivity(intent)

        }

        //회원탈퇴
        binding.ProfileBtnMemberout.setOnClickListener {
            signoutDialog()
        }
        //

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
        val dialogView = LayoutInflater.from(requireContext()).inflate(com.example.mbti_talk.R.layout.dialog_signout, null)
        val binding = SignoutDialogChoice(
            dialogView.findViewById(com.example.mbti_talk.R.id.Signout_btn_check),
            dialogView.findViewById(com.example.mbti_talk.R.id.Signout_btn_cancel)
        )

        val alertDialog = android.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        binding.SignoutBtnCheck.setOnClickListener {
            revokeAccess()
            val intent = Intent(requireContext(), LogInActivity::class.java)
            startActivity(intent)
            alertDialog.dismiss()

        }

        binding.SignoutBtnCancel.setOnClickListener {

            alertDialog.dismiss()
        }

        alertDialog.show()
    }




}







