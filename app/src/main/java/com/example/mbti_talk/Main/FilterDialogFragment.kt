package com.example.mbti_talk.Main

import android.annotation.SuppressLint
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
// FilterDialog 의 동작 정의. 성별, 나이, MBTI 필터 설정을 변경할 수 있는 화면을 제공하고, 유저가 "적용" 버튼을 누르면 설정한 필터를 반영하여 부모 Fragment 에 필터 정보 전달

class FilterDialogFragment : DialogFragment() {

    // FilterDialog 설정값 전달할 리스너 초기화
    private lateinit var buttonClickListener: OnDialogChipClickListener

    // 유저가 선택한 필터 설정을 저장할 변수 초기화
    var gender_male = ""
    var gender_female = ""
    var minValue = 0 // 최소나이
    var maxValue = 0 // 최대나이
    var mbtiEI = ""
    var mbtiSN = ""
    var mbtiTF = ""
    var mbtiJP = ""

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDialogFilterBinding.inflate(inflater, container, false)

        // 각 필터 설정 요소에 대한 변경 이벤트 처리 설정

        binding.filterChipMan.setOnClickListener {
            binding.filterChipMan.setSelected(!binding.filterChipMan.isSelected)
        }
        binding.filterChipWoman.setOnClickListener {
            binding.filterChipWoman.setSelected(!binding.filterChipWoman.isSelected)
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
            // 설정 필터 값 가져온 후, 유효성 검사 수행

            gender_male = if(binding.filterChipMan.isSelected){"남자"}else{""}
            gender_female = if(binding.filterChipWoman.isSelected){"여자"}else{""}


            minValue = binding.filterSliderAge.values[0].toInt()
            maxValue = binding.filterSliderAge.values[1].toInt()

//            if (binding.chipGroupMbtiEI.checkedChipId == View.NO_ID ||
//                binding.chipGroupMbtiSN.checkedChipId == View.NO_ID ||
//                binding.chipGroupMbtiTF.checkedChipId == View.NO_ID ||
//                binding.chipGroupMbtiJP.checkedChipId == View.NO_ID) {
//
//                // 하나 이상의 Chip 그룹이 선택되지 않았을 때
//                Toast.makeText(requireContext(), "MBTI를 모두 선택해주세요.", Toast.LENGTH_SHORT).show()
//            } else {
//                // 설정한 필터를 리스너 통해 전달 후 다이얼로그 닫기
                buttonClickListener.onChipApply(gender_male, gender_female, minValue, maxValue, mbtiEI, mbtiSN, mbtiTF, mbtiJP)
//            }

            Log.d("FilterDialogFragment","#jblee > $gender_male, $gender_female, $minValue, $maxValue,$mbtiEI,$mbtiSN,$mbtiTF,$mbtiJP")
            dismiss()

        }
        return binding.root // 뷰 바인딩 반환하여 화면에 표시
    }

    // 필터 설정 다이얼로그에서 사용할 리스너 정의
    interface OnDialogChipClickListener {
        fun onChipApply(gender_male: String, gender_female: String, minValue: Int, maxValue: Int,mbtiEI: String, mbtiSN: String, mbtiTF: String, mbtiJP: String)
    }

    // 필터 설정 다이얼로그에서 사용할 리스너 설정하는 메서드
    fun setChipClickListener(chipClickListener: OnDialogChipClickListener) {
        Log.d("FilterDialogFragment","#jblee > setChipClickListener")
        // 필터 설정 리스너를 설정
        this.buttonClickListener = chipClickListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            // 다이얼로그 초기 설정을 적용
        }
    }
}

