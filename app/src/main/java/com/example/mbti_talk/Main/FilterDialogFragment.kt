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

class FilterDialogFragment : DialogFragment() {
    var buttonClickListener: OnDialogChipClickListener? = null

    var gender = ""
    var minValue = 0
    var maxValue = 0
    var mbtiEI = ""
    var mbtiSN = ""
    var mbtiTF = ""
    var mbtiJP = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDialogFilterBinding.inflate(inflater, container, false)

        binding.chipGroupGender.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.filter_chip_man -> {
                    gender = "남자"
                }
                R.id.filter_chip_woman -> {
                    gender = "여자"
                }
                R.id.filter_chip_both -> {
                    gender = "둘다"
                }
            }
        }

        binding.filterSliderAge.addOnChangeListener { slider, value, fromUser ->

            val value = slider.values

            minValue = value[0].toInt()
            maxValue = value[1].toInt()
        }

        binding.chipGroupMbtiEI.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.filter_chip_E -> {
                    mbtiEI = "E"
                }
                R.id.filter_chip_I -> {
                    mbtiEI = "I"
                } }
        }

        binding.chipGroupMbtiSN.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.filter_chip_S -> {
                    mbtiSN = "S"
                }
                R.id.filter_chip_N -> {
                    mbtiSN = "N"
                }
            }
        }

        binding.chipGroupMbtiTF.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.filter_chip_T -> {
                    mbtiTF = "T"
                }
                R.id.filter_chip_F -> {
                    mbtiTF = "F"
                }
            }
        }

        binding.chipGroupMbtiJP.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.filter_chip_J -> {
                    mbtiJP = "J"
                }
                R.id.filter_chip_P -> {
                    mbtiJP = "P"
                }
            }
        }

        // "적용" 버튼 클릭 이벤트 처리
        binding.applyBtn.setOnClickListener {
            dismiss()

            if (binding.chipGroupGender.checkedChipId == View.NO_ID ||
                binding.chipGroupMbtiEI.checkedChipId == View.NO_ID ||
                binding.chipGroupMbtiSN.checkedChipId == View.NO_ID ||
                binding.chipGroupMbtiTF.checkedChipId == View.NO_ID ||
                binding.chipGroupMbtiJP.checkedChipId == View.NO_ID) {
                // 하나 이상의 Chip 그룹이 선택되지 않았을 때
                Toast.makeText(requireContext(), "MBTI를 모두 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                buttonClickListener?.onChipGroubGender(gender)
                buttonClickListener?.filterSliderAge(minValue, maxValue)
                buttonClickListener?.onChipGroupMbtiEI(mbtiEI)
                buttonClickListener?.onChipGroupMbtiSN(mbtiSN)
                buttonClickListener?.onChipGroupMbtiTF(mbtiTF)
                buttonClickListener?.onChipGroupMbtiJP(mbtiJP)
                // 선택된 옵션에 대한 처리
                Log.d("jblee", "slider min=$minValue, max = $maxValue")
            }
        }
        return binding.root
    }
    interface OnDialogChipClickListener {
        fun onChipGroubGender(gender: String)
        fun filterSliderAge(minValue: Int, maxValue: Int)
        fun onChipGroupMbtiEI(mbtiEI: String)
        fun onChipGroupMbtiSN(mbtiSN: String)
        fun onChipGroupMbtiTF(mbtiTF: String)
        fun onChipGroupMbtiJP(mbtiJP: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
        }
    }

}

