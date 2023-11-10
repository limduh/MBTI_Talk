package nb_.mbti_talk.Chat

import java.io.Serializable

data class User(
    val name: String? = "",
    val uid: String? = "",
    val email: String? = ""
): Serializable {
}