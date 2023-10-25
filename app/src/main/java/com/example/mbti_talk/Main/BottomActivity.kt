package com.example.mbti_talk.Main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mbti_talk.R
import com.example.mbti_talk.databinding.ActivityBottomBinding
import com.example.mbti_talk.FriendFind.FriendFindFragment
import com.example.mbti_talk.post.PostFragment
import com.example.mbti_talk.Chat.ChatFragment
import com.example.mbti_talk.FriendList.FriendListFragment
import com.example.mbti_talk.MyProfile.MyProfileFragment



class BottomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBottomBinding
    private lateinit var currentFragment: Fragment
    private lateinit var fragmentManager: FragmentManager

    //각 프래그먼트 선언

    private val friendListFragment = FriendListFragment()
    private val postFragment = PostFragment()
    private val friendFindFragment = FriendFindFragment()
    private val chatFragment = ChatFragment()
    private val myProfileFragment = MyProfileFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentManager = supportFragmentManager
        //초기화면 설정 ▽

        supportFragmentManager.beginTransaction().replace(R.id.Bottom_frame, FriendFindFragment()).commit()

        fragmentManager.beginTransaction().replace(R.id.Bottom_frame, friendListFragment).commit()
        currentFragment = friendListFragment


        // 하단 탭이 눌렸을 때 화면을 전환하기 위해선 이벤트 처리하기 위해 BottomNavigationView 객체 생성
        val bnv_main = binding.BottomNav
        bnv_main.setOnItemSelectedListener { menuItem ->
            Log.d("BottomActivity", "bnv_main menuItem.title => ${menuItem.title}")
            when (menuItem.title) {
                "친구목록" -> {
                    switchFragment(friendListFragment)
                    true
                }

                "게시판" -> {
                    switchFragment(postFragment)
                    true
                }

                "친구찾기" -> {
                    switchFragment(friendFindFragment)
                    true
                }

                "채팅" -> {
                    switchFragment(chatFragment)
                    true
                }

                "내 프로필" -> {
                    switchFragment(myProfileFragment)
                    true
                }

                else -> false
            }
        }
    }
    private fun switchFragment(fragment: Fragment) {
        Log.d("BottomActivity", "switchFragment => ${fragment.tag}")

        if (fragment != currentFragment) {
            fragmentManager.beginTransaction().replace(R.id.Bottom_frame, fragment).commit()
            currentFragment = fragment
        }
    }
}


