package com.zabi.travelmemories.ui.addmemory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.zabi.travelmemories.R
import com.zabi.travelmemories.databinding.FragmentAddMemoryBinding


class AddMemoryFragment : Fragment() {

    private var _binding: FragmentAddMemoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val addMemoryViewModel =
            ViewModelProvider(this)[AddMemoryViewModel::class.java]

        _binding = FragmentAddMemoryBinding.inflate(inflater, container, false)

        val spinner: Spinner = binding.spinner

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.moods_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        // Optional: set a listener to respond to item selections
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                    // An item was selected. You can retrieve the selected item using
                    val mood = parent.getItemAtPosition(pos).toString()
                    // Use the mood
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Another interface callback
                }
            }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


