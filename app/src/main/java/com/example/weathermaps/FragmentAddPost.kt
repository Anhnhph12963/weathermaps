package com.example.weathermaps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weathermaps.databinding.FragmentAddPostBinding

class FragmentAddPost : Fragment() {
    private var binding: FragmentAddPostBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPostBinding.inflate(layoutInflater)
        return binding?.root
    }
}