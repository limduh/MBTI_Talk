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
// FilterDialog 의 동작 정의. 성별, 나이, MBTI 필터 설정을 변경할 수 있는 화면을 제공하고, 유저가 "적용" 버튼을 누르면 설정한 필터를 반영하여 부모 Fragment 에 필터 정보 전달

class FilterDialogFragment : DialogFragment() {

    // FilterDialog 설정값 전달할 리스너 초기화
    private lateinit var buttonClickListener: OnDialogChipClickListener

    // 유저가 선택한 필터 설정을 저장할 변수 초기화
    var gender_male = ""
    var gender_female = ""
    var minValue = 0 // 최소나이
    var maxValue = 0 // 최대나이
    var mbtiE = ""
    var mbtiI = ""
    var mbtiS = ""
    var mbtiN = ""
    var mbtiT = ""
    var mbtiF = ""
    var mbtiJ = ""
    var mbtiP = ""

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT // 다이얼로그 너비
            val height = ViewGroup.LayoutParams.MATCH_PARENT // 다이얼로그 높이
            dialog.window?.setLayout(width, height)
        }
    }

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

        binding.filterChipE.setOnClickListener {
            binding.filterChipE.isSelected = !binding.filterChipE.isSelected
            if (binding.filterChipE.isSelected) {
                binding.filterChipI.isSelected = false
            }
        }

        binding.filterChipI.setOnClickListener {
            binding.filterChipI.isSelected = !binding.filterChipI.isSelected
            if (binding.filterChipI.isSelected) {
                binding.filterChipE.isSelected = false
            }
        }

        binding.filterChipS.setOnClickListener {
            binding.filterChipS.isSelected = !binding.filterChipS.isSelected
            if (binding.filterChipS.isSelected) {
                binding.filterChipN.isSelected = false
            }
        }

        binding.filterChipN.setOnClickListener {
            binding.filterChipN.isSelected = !binding.filterChipN.isSelected
            if (binding.filterChipN.isSelected) {
                binding.filterChipS.isSelected = false
            }
        }

        binding.filterChipT.setOnClickListener {
            binding.filterChipT.isSelected = !binding.filterChipT.isSelected
            if (binding.filterChipT.isSelected) {
                binding.filterChipF.isSelected = false
            }
        }

        binding.filterChipF.setOnClickListener {
            binding.filterChipF.isSelected = !binding.filterChipF.isSelected
            if (binding.filterChipF.isSelected) {
                binding.filterChipT.isSelected = false
            }
        }

        binding.filterChipJ.setOnClickListener {
            binding.filterChipJ.isSelected = !binding.filterChipJ.isSelected
            if (binding.filterChipJ.isSelected) {
                binding.filterChipP.isSelected = false
            }
        }

        binding.filterChipP.setOnClickListener {
            binding.filterChipP.isSelected = !binding.filterChipP.isSelected
            if (binding.filterChipP.isSelected) {
                binding.filterChipJ.isSelected = false
            }
        }

        // "뒤로가기" 버튼 클릭 이벤트 처리
        binding.filterBtnBack.setOnClickListener {
            dismiss() // 다이얼로그 닫기
        }

        // "적용" 버튼 클릭 이벤트 처리
        binding.applyBtn.setOnClickListener {

            // 설정 필터 값 가져온 후, 유효성 검사 수행
            gender_male = if(binding.filterChipMan.isSelected){"남자"}else{""}
            gender_female = if(binding.filterChipWoman.isSelected){"여자"}else{""}


            minValue = binding.filterSliderAge.values[0].toInt()
            maxValue = binding.filterSliderAge.values[1].toInt()

            mbtiE = if(binding.filterChipE.isSelected){"E"}else{""}
            mbtiI = if(binding.filterChipI.isSelected){"I"}else{""}
            mbtiS = if(binding.filterChipS.isSelected){"S"}else{""}
            mbtiN = if(binding.filterChipN.isSelected){"N"}else{""}
            mbtiT = if(binding.filterChipT.isSelected){"T"}else{""}
            mbtiF = if(binding.filterChipF.isSelected){"F"}else{""}
            mbtiJ = if(binding.filterChipJ.isSelected){"J"}else{""}
            mbtiP = if(binding.filterChipP.isSelected){"P"}else{""}

            // 설정한 필터를 리스너 통해 전달 후 다이얼로그 닫기
            buttonClickListener.onChipApply(gender_male, gender_female, minValue, maxValue, mbtiE, mbtiI, mbtiS, mbtiN, mbtiT, mbtiF, mbtiJ, mbtiP)

            Log.d("FilterDialogFragment","#jblee > $gender_male, $gender_female, $minValue, $maxValue,$mbtiE,$mbtiI,$mbtiS,$mbtiN,$mbtiT,$mbtiF,$mbtiJ,$mbtiP")
            dismiss()

        }
        return binding.root // 뷰 바인딩 반환하여 화면에 표시
    }

    // 필터 설정 다이얼로그에서 사용할 리스너 정의
    interface OnDialogChipClickListener {
        fun onChipApply(gender_male: String, gender_female: String, minValue: Int, maxValue: Int, mbtiE : String, mbtiI : String, mbtiS : String, mbtiN : String, mbtiT : String, mbtiF : String, mbtiJ : String, mbtiP : String)
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
