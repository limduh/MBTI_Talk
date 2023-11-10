package nb_.mbti_talk.MBTI

object Mbti {
    enum class GRADE {
        A, B, C, D
    }
    // compatData hashMap은 MBTI 유형을 키로 가지며, 각 MBTI 유형에 대한 호환성 데이터를 값으로(A,B,C,D)
    val compatData = hashMapOf(
        "INFJ" to hashMapOf(
            GRADE.A to listOf("ENFP", "ENTP"),
            GRADE.B to listOf("INFP", "INFJ", "ENFJ", "INTJ", "ENTJ", "INTP"),
            GRADE.C to listOf(),
            GRADE.D to listOf("ISFP", "ESFP", "ISTP", "ESTP", "ISFJ", "ESFJ", "ISTJ", "ESTJ")
        ),
        "INFP" to hashMapOf(
            GRADE.A to listOf("ENFJ", "ENTJ"),
            GRADE.B to listOf("INFP", "ENFP", "INFJ", "INTJ", "ENTP", "INTP"),
            GRADE.C to listOf(),
            GRADE.D to listOf("ISFP", "ESFP", "ISTP", "ESTP", "ISFJ", "ESFJ", "ISTJ", "ESTJ")
        ),
        "INTJ" to hashMapOf(
            GRADE.A to listOf("ENFP", "ENTP"),
            GRADE.B to listOf("INFP", "INFJ", "ENFJ", "INTJ", "ENTJ", "INTP"),
            GRADE.C to listOf("ISFP", "ESFP", "ISTP", "ESTP", "ISFJ", "ESFJ", "ISTJ", "ESTJ"),
            GRADE.D to listOf()
        ),
        "INTP" to hashMapOf(
            GRADE.A to listOf("ENTJ"),
            GRADE.B to listOf("INFP", "ENFP", "INFJ", "ENFJ", "INTJ", "ENTP", "INTP"),
            GRADE.C to listOf("ISFP", "ESFP", "ISTP", "ESTP", "ISFJ", "ESFJ", "ISTJ", "ESTJ"),
            GRADE.D to listOf()
        ),
        "ENFJ" to hashMapOf(
            GRADE.A to listOf("INFP", "ISFP"),
            GRADE.B to listOf("ENFP", "INFJ", "ENFJ", "INTJ", "ENTJ", "INTP", "ENTP"),
            GRADE.C to listOf(),
            GRADE.D to listOf("ESFP", "ISTP", "ESTP", "ISFJ", "ESFJ", "ISTJ", "ESTJ")
        ),
        "ENFP" to hashMapOf(
            GRADE.A to listOf("INFJ", "INTJ"),
            GRADE.B to listOf("INFP", "ENFP", "ENFJ", "ENTJ", "INTP", "ENTP"),
            GRADE.C to listOf(),
            GRADE.D to listOf("ISFP", "ESFP", "ISTP", "ESTP", "ISFJ", "ESFJ", "ISTJ", "ESTJ")
        ),
        "ENTJ" to hashMapOf(
            GRADE.A to listOf("INFP", "INTP"),
            GRADE.B to listOf("ENFP", "INFJ", "ENFJ", "INTJ", "ENTJ", "INTP", "ENTP"),
            GRADE.C to listOf("ISFP", "ESFP", "ISTP", "ESTP", "ISFJ", "ESFJ", "ISTJ", "ESTJ"),
            GRADE.D to listOf()
        ),
        "ENTP" to hashMapOf(
            GRADE.A to listOf("INFJ", "INTJ"),
            GRADE.B to listOf("INFP", "ENFP", "ENFJ", "ENTJ", "INTP", "ENTP"),
            GRADE.C to listOf("ISFP", "ESFP", "ISTP", "ESTP", "ISFJ", "ESFJ", "ISTJ", "ESTJ"),
            GRADE.D to listOf()
        ),
        "ISFJ" to hashMapOf(
            GRADE.A to listOf("ESFP", "ESTP"),
            GRADE.B to listOf("ISFJ", "ESFJ", "ISTJ", "ESTJ"),
            GRADE.C to listOf("ISFP", "ISTP", "INTJ", "ENTJ", "INTP", "ENTP"),
            GRADE.D to listOf("INFP", "ENFP", "INFJ", "ENFJ")
        ),
        "ISFP" to hashMapOf(
            GRADE.A to listOf("ENFJ", "ESFJ", "ESTJ"),
            GRADE.B to listOf(),
            GRADE.C to listOf("INTJ", "ENTJ", "INTP", "ENTP", "ISFP", "ESFP", "ISTP", "ESTP", "ISFJ", "ISTJ"),
            GRADE.D to listOf("INFP", "ENFP", "INFJ")
        ),
        "ISTJ" to hashMapOf(
            GRADE.A to listOf("ESTJ", "ESFP", "ESTP"),
            GRADE.B to listOf("ISFJ", "ESFJ", "ISTJ", "ESTJ"),
            GRADE.C to listOf("ISFP", "ISTP", "INTJ", "ENTJ", "INTP", "ENTP"),
            GRADE.D to listOf("INFP", "ENFP", "INFJ", "ENFJ")
        ),
        "ISTP" to hashMapOf(
            GRADE.A to listOf("ESTJ", "ESFJ"),
            GRADE.B to listOf(),
            GRADE.C to listOf("INTJ", "ENTJ", "INTP", "ENTP", "ISFP", "ESFP", "ISTP", "ESTP", "ISFJ", "ISTJ"),
            GRADE.D to listOf("INFP", "ENFP", "INFJ", "ENFJ")
        ),
        "ESFJ" to hashMapOf(
            GRADE.A to listOf("ISFP", "ISTP"),
            GRADE.B to listOf("ISFJ", "ESFJ", "ISTJ", "ESTJ"),
            GRADE.C to listOf("ESFP", "ESTP", "INTJ", "ENTJ", "INTP", "ENTP"),
            GRADE.D to listOf("INFP", "ENFP", "INFJ", "ENFJ")
        ),
        "ESFP" to hashMapOf(
            GRADE.A to listOf("ISFJ", "ISTJ"),
            GRADE.B to listOf(),
            GRADE.C to listOf("ESFP", "ESTP", "INTJ", "ENTJ", "INTP", "ENTP", "ISFP", "ESFJ", "ISTP", "ESTJ"),
            GRADE.D to listOf("INFP", "ENFP", "INFJ", "ENFJ")
        ),
        "ESTJ" to hashMapOf(
            GRADE.A to listOf("ISFP", "ISTP", "INTP"),
            GRADE.B to listOf("ISFJ", "ESFJ", "ISTJ", "ESTJ"),
            GRADE.C to listOf("ESFP", "ESTP", "INTJ", "ENTJ", "ENTP"),
            GRADE.D to listOf("INFP", "ENFP", "INFJ", "ENFJ")
        ),
        "ESTP" to hashMapOf(
            GRADE.A to listOf("ISFJ", "ISTJ"),
            GRADE.B to listOf(),
            GRADE.C to listOf("ESFP", "ESTP", "INTJ", "ENTJ", "INTP", "ENTP", "ISFP", "ESFJ", "ISTP", "ESTJ"),
            GRADE.D to listOf("INFP", "ENFP", "INFJ", "ENFJ")
        ),
    )
}