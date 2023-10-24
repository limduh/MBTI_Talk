package com.example.mbti_talk.Main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mbti_talk.ChatFragment
import com.example.mbti_talk.FriendFindFragment
import com.example.mbti_talk.FriendListFragment
import com.example.mbti_talk.MyProfileFragment
import com.example.mbti_talk.PostFragment
import com.example.mbti_talk.R
import com.example.mbti_talk.databinding.ActivityBottomBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

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
        currentFragment = friendFindFragment

        supportFragmentManager.beginTransaction().replace(R.id.Bottom_frame, FriendFindFragment()).commit()

        // 하단 탭이 눌렸을 때 화면을 전환하기 위해선 이벤트 처리하기 위해 BottomNavigationView 객체 생성
        val bnv_main = binding.BottomNav
        bnv_main.setOnItemSelectedListener { menuItem ->
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

                "내프로필" -> {
                    switchFragment(myProfileFragment)
                    true
                }

                else -> false
            }
        }
    }
    private fun switchFragment(fragment: Fragment) {
        if (fragment != currentFragment) {
            fragmentManager.beginTransaction().replace(R.id.Bottom_frame, fragment).commit()
            currentFragment = fragment
        }
    }
}

        // OnNavigationItemSelectedListener를 통해 탭 아이템 선택 시 이벤트를 처리
        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.

//        bnv_main.run { setOnNavigationItemSelectedListener {
//            when(it.itemId) {
//                R.id.friend -> {
//                    // 다른 프래그먼트 화면으로 이동하는 기능
//                    val friendList = FriendListFragment()
//                    supportFragmentManager.beginTransaction().replace(R.id.Bottom_frame, ).commit()
//                }
//                R.id.post -> {
//                    val boardFragment = BoardFragment()
//                    supportFragmentManager.beginTransaction().replace(R.id.Bottom_frame, boardFragment).commit()
//                }
//                R.id.findfriend -> {
//                    val settingFragment = SettingFragment()
//                    supportFragmentManager.beginTransaction().replace(R.id.Bottom_frame, settingFragment).commit()
//                }
//
//                R.id.chat -> {
//                    val settingFragment = SettingFragment()
//                    supportFragmentManager.beginTransaction().replace(R.id.Bottom_frame, settingFragment).commit()
//                }
//
//                R.id.myprofile -> {
//                    val settingFragment = SettingFragment()
//                    supportFragmentManager.beginTransaction().replace(R.id.Bottom_frame, settingFragment).commit()
//                }
//            }
//            true
//        }
//            selectedItemId = R.id.friend
//        }

