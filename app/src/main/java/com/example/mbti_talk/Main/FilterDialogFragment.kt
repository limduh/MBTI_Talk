package com.example.mbti_talk.Main

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.mbti_talk.databinding.FragmentDialogFilterBinding

class FilterDialogFragment : DialogFragment() {

    var minValue = 0
    var maxValue = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDialogFilterBinding.inflate(inflater, container, false)

        // "적용" 버튼 클릭 이벤트 처리
        binding.applyBtn.setOnClickListener {
            dismiss()

            Log.d("jblee", "slider min=$minValue, max = $maxValue")

        }

        binding.filterSliderAge.addOnChangeListener { slider, value, fromUser ->

            val value = slider.values

            minValue = value[0].toInt()
            maxValue = value[1].toInt()
        }


        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
        }
    }

}

