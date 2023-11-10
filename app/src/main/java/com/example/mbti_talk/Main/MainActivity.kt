package nb_.mbti_talk.Main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import nb_.mbti_talk.Adapter.UserAdapter
import nb_.mbti_talk.databinding.ActivityMainBinding

/* RDB 에서 데이터를 가져와 RecyclerView를 사용하여 화면에 표시
1. MainActivity 클래스 정의
2. 뷰 바인딩을 초기화 -> RDB 에서 데이터를 가져와 목록에 표시
3. onCreate 메서드에서 RDB 로부터 데이터 가져옴.
4. 가져온 데이터를 userList에 추가
5. RecyclerView 어댑터를 초기화 후 데이터 목록을 어댑터에 설정
6. 마지막으로, RDB로부터 데이터를 비동기적으로 가져오고 가져오는 동안 발생한 오류를 처리.
* */

class MainActivity : AppCompatActivity() {

    // MainActivity 클래스의 멤버 변수들을 선언
    lateinit var binding: ActivityMainBinding // 뷰바인딩 초기화
    lateinit var adapter: UserAdapter // RecyclerView 에 사용될 어댑터 초기화


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 뷰 바인딩 초기화, 해당 바인딩을 현재 액티비티 레이아웃으로 설정
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}