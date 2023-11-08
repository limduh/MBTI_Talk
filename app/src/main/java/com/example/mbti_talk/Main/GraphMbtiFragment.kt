package com.example.mbti_talk.Main

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.mbti_talk.R
import com.example.mbti_talk.databinding.FragmentDialogFilterBinding
import com.example.mbti_talk.databinding.FragmentMbtiGraphBinding

// FilterDialog 의 동작 정의. 성별, 나이, MBTI 필터 설정을 변경할 수 있는 화면을 제공하고, 유저가 "적용" 버튼을 누르면 설정한 필터를 반영하여 부모 Fragment 에 필터 정보 전달

class GraphMbtiFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMbtiGraphBinding.inflate(inflater, container, false)

        // 뒤로가기 누르면 현 다이얼로그 종료
        binding.graphBackArrow.setOnClickListener {
            dismiss()
        }
        return binding.root // 뷰 바인딩 반환하여 화면에 표시
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            // 다이얼로그 초기 설정을 적용
        }
    }
}
