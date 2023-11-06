package com.example.mbti_talk.utils


import android.content.Context
import com.example.mbti_talk.MBTI.Mbti

object Utils {
    /**
     * 마지막 검색어를 Shared Preferences에 저장합니다.
     *
     * @param context 호출하는 컨텍스트 (일반적으로 Activity 또는 Application)
     * @param query 저장하려는 검색어 문자열
     */
    fun setMyUid(context: Context, query: String) {
        val prefs = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        prefs.edit().putString("save", query).apply()
    }

    /**
     * Shared Preferences에서 마지막 검색어를 가져옵니다.
     *
     * @param context 호출하는 컨텍스트 (일반적으로 Activity 또는 Application)
     * @return 마지막으로 저장된 검색어 문자열. 저장된 값이 없으면 null을 반환합니다.
     */
    fun getMyUid(context: Context): String? {
        val prefs = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        return prefs.getString("save", null)
    }

    /**
     * 두 개의 MBTI 유형 비교 후 등급 반환
     *
     * myMbti가 호환성 데이터(Mbti.compatData)의 키로 존재하는지 확인
     * myMbti 이 있다면, 해당 MBTI 와 otherMbti 의 호환성 확인(호환성이 없을 경우 기본값 D를 반환)
     */
    fun getCompat(myMbti: String?, otherMbti: String): Mbti.GRADE {
        return when (myMbti) {
            in Mbti.compatData -> {
                val gradeMap = Mbti.compatData[myMbti]
                val grade = when {
                    otherMbti in gradeMap?.get(Mbti.GRADE.A)!! -> Mbti.GRADE.A
                    otherMbti in gradeMap?.get(Mbti.GRADE.B)!! -> Mbti.GRADE.B
                    otherMbti in gradeMap?.get(Mbti.GRADE.C)!! -> Mbti.GRADE.C
                    otherMbti in gradeMap?.get(Mbti.GRADE.D)!! -> Mbti.GRADE.D
                    else -> null
                }
                grade ?: Mbti.GRADE.D // MBTI 유형이 없을 경우 기본값으로 D 반환
            }
            else -> Mbti.GRADE.D // MBTI 유형이 없을 경우 기본값으로 D 반환
        }
    }
    // sharedPref 사용하여 query(유저 mbti 문자열)를 myMbti 에 저장
    fun setMyMbti(context: Context, query: String) {
        val prefs = context.getSharedPreferences("myMbti", Context.MODE_PRIVATE)
        prefs.edit().putString("save", query).apply()
    }

    // sharedPref 사용하여 myMbti 키에 저장된 유저 mbti 유형 검색
    fun getMyMbti(context: Context): String? {
        val prefs = context.getSharedPreferences("myMbti", Context.MODE_PRIVATE)
        return prefs.getString("save", "")
    }

    // 필터 조건을 저장하는 함수
    fun saveFilterConditions(context: Context, ageCondition: Int?, genderCondition: String?, mbtiCondition: String?) {
        val prefs = context.getSharedPreferences("FilterPreferences", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("ageCondition", ageCondition ?: -1) // -1은 조건이 선택되지 않은 상태를 나타냅니다.
        editor.putString("genderCondition", genderCondition)
        editor.putString("mbtiCondition", mbtiCondition)
        editor.apply()
    }

    // 저장된 필터 조건을 불러오는 함수
    fun getFilterConditions(context: Context): Triple<Int?, String?, String?> {
        val prefs = context.getSharedPreferences("FilterPreferences", Context.MODE_PRIVATE)
        val ageCondition = prefs.getInt("ageCondition", -1)
        val genderCondition = prefs.getString("genderCondition", null)
        val mbtiCondition = prefs.getString("mbtiCondition", null)

        return Triple(ageCondition.takeIf { it != -1 }, genderCondition, mbtiCondition)
    }

}