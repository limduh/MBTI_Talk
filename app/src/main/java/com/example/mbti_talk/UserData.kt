package nb_.mbti_talk

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


// FB 와 연결되어 있습니다.
@Parcelize
data class UserData(
    val user_email: String = "",
    val user_age: Int = 0,
    val user_nickName: String = "",
    val user_uid: String = "",
    val user_gender: String = "",
    val user_mbti: String = "",
    val user_profile: String = "",
    var user_compat: String = "" // mbti 호환
) : Parcelable