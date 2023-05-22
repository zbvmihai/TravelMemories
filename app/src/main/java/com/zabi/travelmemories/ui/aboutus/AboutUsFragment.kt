package com.zabi.travelmemories.ui.aboutus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.zabi.travelmemories.databinding.FragmentAboutusBinding

class AboutUsFragment : Fragment() {

    private var _binding: FragmentAboutusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val aboutUsViewModel =
            ViewModelProvider(this)[AboutUsViewModel::class.java]

        _binding = FragmentAboutusBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val tvTitle: TextView = binding.tvTitle
        aboutUsViewModel.title.observe(viewLifecycleOwner) {
            tvTitle.text = it
        }

        val tvAboutUs: TextView = binding.tvAbout
        aboutUsViewModel.aboutus.observe(viewLifecycleOwner) {
            tvAboutUs.text = it
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}