package nb_.mbti_talk.Main

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import nb_.mbti_talk.R
import nb_.mbti_talk.databinding.ActivityBottomBinding
import nb_.mbti_talk.FriendFind.FriendFindFragment
import nb_.mbti_talk.post.PostFragment
import nb_.mbti_talk.Chat.ChatFragment
import nb_.mbti_talk.FriendList.FriendListFragment
import nb_.mbti_talk.MyProfile.MyProfileFragment
import com.tauheed.wavybottomnavigation.WavyBottomNavigation
import com.tauheed.wavybottomnavigation.WavyBottomNavigationCell


class BottomActivity : AppCompatActivity() {

    // 이전에 눌린 시간 기록하는 변수
    private var backKeyPressedTime: Long = 0

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
                    R.drawable.ic_friend
                )
            )
            add(
                WavyBottomNavigation.Model(
                    ID_POST,
                    R.drawable.ic_bulletin
                )
            )
            add(
                WavyBottomNavigation.Model(
                    ID_SEARCH,
                    R.drawable.ic_search
                )
            )
            add(
                WavyBottomNavigation.Model(
                    ID_CHAT,
                    R.drawable.ic_chat
                )
            )
            add(
                WavyBottomNavigation.Model(
                    ID_MYPAGE,
                    R.drawable.ic_mypage
                )
            )


            setOnShowListener {
                val name = when (it.id) {
                    ID_FRIEND -> {
                        binding.BottomFrame
                        switchFragment(friendListFragment)
                    }
                    ID_POST -> {
                        binding.BottomFrame
                        switchFragment(postFragment)
                    }
                    ID_SEARCH -> {
                        binding.BottomFrame
                        switchFragment(friendFindFragment)
                    }
                    ID_CHAT -> {
                        binding.BottomFrame
                        switchFragment(chatFragment)
                    }
                    ID_MYPAGE -> {
                        binding.BottomFrame
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

        }
        val startFragment: String = intent.getStringExtra("startFragment") ?: ""
        if (startFragment == "MyProfileFragment") {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.Bottom_frame,myProfileFragment)
            fragmentTransaction.commit()
            binding.bottomNavigation.show(ID_MYPAGE)
        } else {
            binding.bottomNavigation.show(ID_SEARCH)
        }

    }

    override fun onBackPressed() {
        // 현재 시간과 마지막으로 뒤로가기 버튼이 눌린 시간 차이 계산
        if (System.currentTimeMillis() - backKeyPressedTime < 2000) {
            super.onBackPressed()
            return
        }
        // 유저에게 토스트 메시지를 보여줌
        Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        // 마지막으로 뒤로가기 버튼이 눌린 시간 업데이트
        backKeyPressedTime = System.currentTimeMillis()
    }

    private fun switchFragment(fragment: Fragment) {
        Log.d("BottomActivity", "switchFragment => ${fragment.tag}")

        if (fragment != currentFragment) {
            fragmentManager.beginTransaction().replace(R.id.Bottom_frame, fragment).commit()
            currentFragment = fragment
        }
    }
}

