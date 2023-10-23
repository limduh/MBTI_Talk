package com.example.mbti_talk.utils

import android.app.Activity
import android.content.ClipData.Item
import android.content.Context
import com.example.mbti_talk.UserData
import com.google.gson.GsonBuilder

class Utils {


    /**
     * 마지막 검색어를 Shared Preferences에 저장합니다.
     *
     * @param context 호출하는 컨텍스트 (일반적으로 Activity 또는 Application)
     * @param query 저장하려는 검색어 문자열
     */
    fun saveLastSearch(context: Context, query: String) {
        val prefs = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        prefs.edit().putString("save", query).apply()
    }

    /**
     * Shared Preferences에서 마지막 검색어를 가져옵니다.
     *
     * @param context 호출하는 컨텍스트 (일반적으로 Activity 또는 Application)
     * @return 마지막으로 저장된 검색어 문자열. 저장된 값이 없으면 null을 반환합니다.
     */
    fun getLastSearch(context: Context): String? {
        val prefs = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        return prefs.getString("save", null)
    }
}
