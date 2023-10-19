package com.example.mbti_talk.utils

import android.app.Activity
import android.content.ClipData.Item
import android.content.Context
import com.example.mbti_talk.UserData
import com.google.gson.GsonBuilder

class Utils {


//Utils자료 참고 // 강의내용 참고 코드
    //SharedPreferences와 Preferences의 차이 : 여러개의 정보를 저장할거냐 or 정보한개만 저장할거냐 인데,
    //보통 한개만 하지 않으므로 여러개를 저장해주는 SharedPreferences를 사용한다.

    //getSharedPreferences의 형식 :
    //getSharedPreferences(name, mode)

    //name : 프리퍼런스 데이터를 저장할 xml파일의 이름이다. (자유롭게 설정가능)
    //mode : 파일의 공유 모드이다.

    //여기서 내가 사용한 MODE_PRIVATE은, 생성된 xml파일은, 호출한 애플리케이션내에서
    //(즉,우리 mbtiTalk의 앱 내에서만 ) 읽기 쓰기가 가능하다.

    //GsonBuilder를 사용하기 위해선 그래들에, gson관련
    //    // gson converter
    //    implementation("com.google.code.gson:gson:2.10.1")
    //    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //해당 그래들을 추가해야 사용할 수 있다.

    private fun saveData(context: Context, item: UserData) {
        val prefs = context.getSharedPreferences("UserInfo", Activity.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = GsonBuilder().create()
        editor.putString("save", gson.toJson(item))
        //첫번째는 키, 2번째는 실제 담아둘 값.
        editor.apply()
    }

    private fun loadData(context: Context): ArrayList<Item> {
        val prefs = context.getSharedPreferences("UserInfo", Activity.MODE_PRIVATE)
        //여기서 Activity는 왜쓰이는지 모름. 물어보기
        val allEntries: Map<String, *> = prefs.all
        val load = ArrayList<Item>()
        val gson = GsonBuilder().create()
        for ((key, value) in allEntries) {
            val item = gson.fromJson(value as String, Item::class.java)
            load.add(item)
        }
        return load
    }

}

// 참고 코드
//    fun getPrefBookmarkItems(context: Context): ArrayList<Item> {
//        val prefs = context.getSharedPreferences("videoID", Activity.MODE_PRIVATE)
//        val allEntries: Map<String, *> = prefs.all
//        val bookmarkItems = ArrayList<Item>()
//        val gson = GsonBuilder().create()
//        for ((key, value) in allEntries) {
//            val item = gson.fromJson(value as String, Item::class.java)
//            bookmarkItems.add(item)
//        }
//        return bookmarkItems
//    }


