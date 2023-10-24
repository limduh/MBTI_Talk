package com.example.mbti_talk.Main

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.mbti_talk.FriendFind.FriendFindFragment
import com.example.mbti_talk.R

// MainFriendActivity 클래스는 Fragment를 활성화하고 화면에 표시
class MainFriendActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_friend_find) // fragment_main_friend.xml 레이아웃 화면에 표시

        // MainFriendFragment를 활성화
        supportFragmentManager.beginTransaction()
            .replace(R.id.Friend_list_frag, FriendFindFragment())
            .commit()
    }
}

/* MainFriendFragment를 사용하여 앱의 메인 화면을 구성합니다.
* supportFragmentManager를 사용하여 Fragment를 관리. Fragment의 추가, 교체 및 제거를 처리
* beginTransaction() 함수를 호출하여 Fragment 트랜잭션 시작.
* replace() 함수로 R.id.const_main_friend_frag의 위치에 MainFriendFragment를 추가.
* commit() 함수를 호출하여 트랜잭션을 실행하고 MainFriendFragment를 화면에 표시*/