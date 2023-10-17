package com.example.mbti_talk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    companion object {
//        const val HOME_ITEM = R.id.homeFragment  서치프래그먼트없음
//        const val OFFERS_ITEM = R.id.offersFragment
//        const val MORE_ITEM = R.id.moreFragment
//        const val SECTION_ITEM = R.id.sectionFragment
//        const val CART_ITEM = R.id.cartFragment
//        const val BLANK_ITEM = R.id.blankFragment 바텀에 연결할 페이지가 없음
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


}