package com.example.mbti_talk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.mbti_talk.databinding.ActivityTestBinding
import com.example.mbti_talk.post.PostFragment

class TestActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTestBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            fragment1Btn.setOnClickListener {
                setFragment(PostFragment())
            }

        }
        setFragment(PostFragment()) // 프로그램 시작화면을 FirstFragment로 시작하기 위해서 씀
    }

    private fun setFragment(frag : Fragment) { // 코틀린 코드에서 동적으로 프래그먼트 추가
        supportFragmentManager.commit { // supportFragmentManager: 사용자 상호작용에 응답해 Fragment를 추가하거나 삭제한다.
            replace(R.id.frameLayout, frag) // 어느 프레임 레이아웃을 띄울것이냐, 어떤 프래그먼트인가 결정
            setReorderingAllowed(true) // 애니메이션과 전환을 올바르게 작동하도록 트랜잭션과 관련된 프래그먼트의 상태 변경을 최적화
            addToBackStack("") //뒤로가기 버튼 클릭시 다음 액션(이전 프래그먼트로 가거나 앱이 종료되거나)
        }
    }
}