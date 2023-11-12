package nb_.mbti_talk.Main

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import nb_.mbti_talk.databinding.FragmentMbtiGraphBinding
import kotlin.math.max

// FilterDialog 의 동작 정의. 성별, 나이, MBTI 필터 설정을 변경할 수 있는 화면을 제공하고, 유저가 "적용" 버튼을 누르면 설정한 필터를 반영하여 부모 Fragment 에 필터 정보 전달

class GraphMbtiFragment : DialogFragment() {

    // ScaleGestureDetector와 ImageView를 위한 변수 선언
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 뷰 바인딩을 사용하여 레이아웃 인플레이트
        val binding = FragmentMbtiGraphBinding.inflate(inflater, container, false)

        imageView = binding.mbtiGraph // 이미지뷰 초기화
        // ScaleGestureDetector를 초기화, 커스텀 ScaleListener 클래스를 제스처 리스너로 설정
        scaleGestureDetector = ScaleGestureDetector(requireContext(), ScaleListener())

        // 이미지 뷰에 onTouchListener를 설정하여 ScaleGestureDetector가 터치 이벤트를 처리
        imageView.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            true
        }

        // 뒤로가기 누르면 현 다이얼로그 종료
        binding.graphBackArrow.setOnClickListener {
            dismiss()
        }
        return binding.root // 뷰 바인딩 반환하여 화면에 표시
    }

    // ScaleGestureDetector를 위한 내부 클래스
    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        // 사용자가 핀치 줌 제스처를 사용할 때 호출
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor // scaleFactor에 사용자가 줌한 비율을 곱함

            // scaleFactor 가 1.0 미만이 되지 않도록 하여 줌아웃 방지
            scaleFactor = max(1.0f, scaleFactor)

            imageView.scaleX = scaleFactor // 이미지뷰의 x축 크기를 scaleFactor로 설정
            imageView.scaleY = scaleFactor // 이미지뷰의 y축 크기를 scaleFactor로 설정
            return true
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            // 다이얼로그 초기 설정을 적용
        }
    }
}
