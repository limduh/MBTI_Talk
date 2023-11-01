package com.example.mbti_talk.Main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mbti_talk.R
import com.example.mbti_talk.databinding.FragmentDialogFilterBinding

class FilterDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDialogFilterBinding.inflate(inflater, container, false)

        binding.filterChipE.setOnClickListener {
            binding.filterChipE.isChecked = !binding.filterChipE.isChecked
        }

        return binding.root
    }
}

