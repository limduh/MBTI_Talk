package com.example.mbti_talk.MBTI

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mbti_talk.Main.BottomActivity
import com.example.mbti_talk.R

class MbtiquestFragment : Fragment() {
    private var questionType: Int = 0

    private val questionTile = listOf(
        R.string.question1_title,
        R.string.question2_title,
        R.string.question3_title,
        R.string.question4_title
    )

    private val questionTexts = listOf(
        listOf(R.string.question1_1, R.string.question1_2, R.string.question1_3, R.string.question1_4, R.string.question1_5),
        listOf(R.string.question2_1, R.string.question2_2, R.string.question2_3, R.string.question2_4, R.string.question2_5),
        listOf(R.string.question3_1, R.string.question3_2, R.string.question3_3, R.string.question3_4, R.string.question3_5),
        listOf(R.string.question4_1, R.string.question4_2, R.string.question4_3, R.string.question4_4, R.string.question4_5)
    )

    private val questionAnswers = listOf(
        listOf(
            listOf(R.string.question1_1_answer1, R.string.question1_1_answer2),
            listOf(R.string.question1_2_answer1, R.string.question1_2_answer2),
            listOf(R.string.question1_3_answer1, R.string.question1_3_answer2),
            listOf(R.string.question1_4_answer1, R.string.question1_4_answer2),
            listOf(R.string.question1_5_answer1, R.string.question1_5_answer2)
        ),
        listOf(
            listOf(R.string.question2_1_answer1, R.string.question2_1_answer2),
            listOf(R.string.question2_2_answer1, R.string.question2_2_answer2),
            listOf(R.string.question2_3_answer1, R.string.question2_3_answer2),
            listOf(R.string.question2_4_answer1, R.string.question2_4_answer2),
            listOf(R.string.question2_5_answer1, R.string.question2_5_answer2)
        ),
        listOf(
            listOf(R.string.question3_1_answer1, R.string.question3_1_answer2),
            listOf(R.string.question3_2_answer1, R.string.question3_2_answer2),
            listOf(R.string.question3_3_answer1, R.string.question3_3_answer2),
            listOf(R.string.question3_4_answer1, R.string.question3_4_answer2),
            listOf(R.string.question3_5_answer1, R.string.question3_5_answer2)
        ),
        listOf(
            listOf(R.string.question4_1_answer1, R.string.question4_1_answer2),
            listOf(R.string.question4_2_answer1, R.string.question4_2_answer2),
            listOf(R.string.question4_3_answer1, R.string.question4_3_answer2),
            listOf(R.string.question4_4_answer1, R.string.question4_4_answer2),
            listOf(R.string.question4_5_answer1, R.string.question4_5_answer2)
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            questionType = it.getInt(ARG_QUESTION_TYPE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.activity_mbti_quest, container, false)

        val title: TextView = view.findViewById(R.id.tv_question_title)
        title.text = getString(questionTile[questionType])

        val questionTextView = listOf<TextView>(
            view.findViewById(R.id.tv_question_1),
            view.findViewById(R.id.tv_question_2),
            view.findViewById(R.id.tv_question_3),
            view.findViewById(R.id.tv_question_4),
            view.findViewById(R.id.tv_question_5)
        )

        val answerRadioGroup = listOf<RadioGroup>(
            view.findViewById(R.id.rg_answer_1),
            view.findViewById(R.id.rg_answer_2),
            view.findViewById(R.id.rg_answer_3),
            view.findViewById(R.id.rg_answer_4),
            view.findViewById(R.id.rg_answer_5)
        )


        for (i in questionTextView.indices){
            questionTextView[i].text = getString(questionTexts[questionType][i])

            val radioButton1 = answerRadioGroup[i].getChildAt(0) as RadioButton
            val radioButton2 = answerRadioGroup[i].getChildAt(1) as RadioButton

            radioButton1.text = getString(questionAnswers[questionType][i][0])
            radioButton2.text = getString(questionAnswers[questionType][i][1])
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mbtiQuestionBack: ImageView = view.findViewById(R.id.mbti_question_back)
        mbtiQuestionBack.setOnClickListener {
            val bottomActivityIntent = Intent(activity, BottomActivity::class.java)
            startActivity(bottomActivityIntent)
            activity?.finish() // 현재 Fragment가 속한 Activity 종료
        }

        val answerRadioGroup = listOf<RadioGroup>(
            view.findViewById(R.id.rg_answer_1),
            view.findViewById(R.id.rg_answer_2),
            view.findViewById(R.id.rg_answer_3),
            view.findViewById(R.id.rg_answer_4),
            view.findViewById(R.id.rg_answer_5)
        )

        val btn_next : Button = view.findViewById(R.id.btn_next)


        btn_next.setOnClickListener {

            val isAllAnswered = answerRadioGroup.all { it.checkedRadioButtonId != -1 }

            if(isAllAnswered) {
                val responses = answerRadioGroup.map {radioGroup ->   // << 2,1,2
                    val firstRadioButton = radioGroup.getChildAt(0) as RadioButton
                    if(firstRadioButton.isChecked) 1 else 2
                }

                (activity as? MbtiTestActivity)?.questionnaireResults?.addResponses(responses)
                (activity as? MbtiTestActivity)?.moveToNextQuestion()

            }else {
                Toast.makeText(context, "모든 질문에 답해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }






    companion object {
        private const val ARG_QUESTION_TYPE = "questionType"

        fun newInstance(questionType: Int): MbtiquestFragment {
            val fragment = MbtiquestFragment()
            val args = Bundle()
            args.putInt(ARG_QUESTION_TYPE, questionType)
            fragment.arguments = args
            return fragment
        }
    }
}
