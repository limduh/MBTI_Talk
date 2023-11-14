package com.example.mbti_talk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import nb_.mbti_talk.R


class Dialog_unblockCheckFragment(val callback: (Boolean) -> Unit) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 다이얼로그의 뷰를 인플레이트합니다.
        val view = inflater.inflate(R.layout.fragment_dialog_unblock_check, container, false)

        // '확인' 버튼을 찾고 클릭 이벤트를 설정합니다.
        val unblockCheckButton = view.findViewById<Button>(R.id.unblock_check)
        unblockCheckButton.setOnClickListener {
            callback(true) // '확인' 버튼을 클릭했을 때
            dismiss() // 다이얼로그 닫기
        }

        // '취소' 버튼을 찾고 클릭 이벤트를 설정합니다.
        val unblockCancelButton = view.findViewById<Button>(R.id.unblock_cancel)
        unblockCancelButton.setOnClickListener {
            callback(false) // '취소' 버튼을 클릭했을 때
            dismiss() // 다이얼로그 닫기
        }

        return view
    }
}