package com.livmas.itertable.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.livmas.itertable.databinding.FragmentAlarmBinding

class AlarmFragment : Fragment() {
    private lateinit var binding: FragmentAlarmBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlarmBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            bClose.setOnClickListener {
                root.visibility = View.GONE
            }
            bReset.setOnClickListener {
                SetAlarmDialog().show(requireActivity().supportFragmentManager, "alarm")
            }
        }
    }
}