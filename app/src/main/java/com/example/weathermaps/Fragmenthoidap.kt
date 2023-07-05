package com.example.weathermaps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weathermaps.databinding.FragmentHoidapBinding

class Fragmenthoidap : Fragment() {

    private var binding: FragmentHoidapBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHoidapBinding.inflate(layoutInflater)
        return binding?.root
    }
}