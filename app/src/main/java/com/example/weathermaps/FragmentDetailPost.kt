package com.example.weathermaps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weathermaps.databinding.FragmentDetailPostBinding

class FragmentDetailPost : Fragment() {
    private var binding: FragmentDetailPostBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailPostBinding.inflate(layoutInflater)
        return binding?.root
    }
}