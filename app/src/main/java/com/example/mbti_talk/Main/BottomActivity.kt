package com.example.mbti_talk.Main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mbti_talk.R
import com.example.mbti_talk.databinding.ActivityBottomBinding
import com.example.mbti_talk.FriendFind.FriendFindFragment
import com.example.mbti_talk.post.PostFragment
import com.example.mbti_talk.Chat.ChatFragment
import com.example.mbti_talk.FriendList.FriendListFragment
import com.example.mbti_talk.MyProfile.MyProfileFragment
import com.tauheed.wavybottomnavigation.WavyBottomNavigation
import com.tauheed.wavybottomnavigation.WavyBottomNavigationCell


class BottomActivity : AppCompatActivity() {

    companion object {
        private const val ID_FRIEND = 0
        private const val ID_POST = 1
        private const val ID_SEARCH = 2
        private const val ID_CHAT = 3
        private const val ID_MYPAGE = 4
    }



    private lateinit var binding: ActivityBottomBinding
    private lateinit var currentFragment: Fragment
    private lateinit var fragmentManager: FragmentManager

    //각 프래그먼트 선언asdfasdfasdf

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
        fragmentManager.beginTransaction().replace(R.id.Bottom_frame, friendFindFragment).commit()
        currentFragment = friendFindFragment

        binding.bottomNavigation.apply {
            add(
                WavyBottomNavigation.Model(
                    ID_FRIEND,
//                    R.drawable.ic_home
                    R.drawable.ic_friend
                )
            )
            add(
                WavyBottomNavigation.Model(
                    ID_POST,
//                    R.drawable.ic_explore
                    R.drawable.ic_bulletin
                )
            )
            add(
                WavyBottomNavigation.Model(
                    ID_SEARCH,
//                    R.drawable.ic_message
                    R.drawable.ic_search
                )
            )
            add(
                WavyBottomNavigation.Model(
                    ID_CHAT,
//                    R.drawable.ic_notification
                    R.drawable.ic_chat
                )
            )
            add(
                WavyBottomNavigation.Model(
                    ID_MYPAGE,
//                    R.drawable.ic_account
                    R.drawable.ic_mypage
                )
            )

            setCount(ID_CHAT, "3")

            setOnShowListener {
                val name = when (it.id) {
                    ID_FRIEND -> {
//                        binding.BottomFrame.setBackgroundColor(Color.parseColor("#D84879"))
                        binding.BottomFrame
                        switchFragment(friendListFragment)
                        "HOME"
                    }
                    ID_POST -> {
//                        binding.BottomFrame.setBackgroundColor(Color.parseColor("#4CAF50"))
                        binding.BottomFrame
                        switchFragment(postFragment)
                        "EXPLORE"
                    }
                    ID_SEARCH -> {
//                        binding.BottomFrame.setBackgroundColor(Color.parseColor("#ffa500"))
                        binding.BottomFrame
                        "MESSAGE"
                        switchFragment(friendFindFragment)
                    }
                    ID_CHAT -> {
//                        binding.BottomFrame.setBackgroundColor(Color.parseColor("#ff69b4"))
                        binding.BottomFrame
                        "NOTIFICATION"
                        switchFragment(chatFragment)
                    }
                    ID_MYPAGE -> {
//                        binding.BottomFrame.setBackgroundColor(Color.parseColor("#6495ed"))
                        binding.BottomFrame
                        "ACCOUNT"
                        switchFragment(myProfileFragment)
                    }
                    else -> ""
                }

            }

            setOnClickMenuListener {
                val name = when (it.id) {
                    ID_FRIEND -> "FRIEND"
                    ID_POST -> "POST"
                    ID_SEARCH -> "SEARCH"
                    ID_CHAT -> "CHAT"
                    ID_MYPAGE -> "MYPAGE"
                    else -> ""
                }
            }

            setOnReselectListener {
                Toast.makeText(context, "item ${it.id} is reselected.", Toast.LENGTH_LONG).show()
            }

            show(ID_SEARCH)

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


