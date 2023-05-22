package com.zabi.travelmemories.ui.contactus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zabi.travelmemories.databinding.FragmentContactusBinding

class ContactUsFragment : Fragment() {

    private var _binding: FragmentContactusBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentContactusBinding.inflate(inflater, container, false)

        binding.fabEmail.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:TravelMemories@contact.com")
                putExtra(Intent.EXTRA_SUBJECT, "Feedback/Inquiry")
            }
            startActivity(emailIntent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}