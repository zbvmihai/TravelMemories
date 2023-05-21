package com.zabi.travelmemories.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zabi.travelmemories.R
import com.zabi.travelmemories.adapters.MemoriesAdapter
import com.zabi.travelmemories.databinding.FragmentHomeBinding
import com.zabi.travelmemories.models.Location
import com.zabi.travelmemories.models.Memory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val location1 = Location(23,23)
        val memory1 = Memory(1,"Plimbare la Brasov","imgUrl","20/10/2023","Brasov", location1
            ,"CarTravel",50.0,"Plimbare cu masina la Brasov" )

        val memoryList = arrayListOf<Memory>()

        memoryList.add(memory1)

        setupMemoryRecyclerView(memoryList)

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_addMemoryFragment)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupMemoryRecyclerView(memoryList: ArrayList<Memory>){

        binding.rvHome.layoutManager = LinearLayoutManager(activity)

        val memoryAdapter = activity?.let { MemoriesAdapter(it,memoryList) }
        binding.rvHome.adapter = memoryAdapter


        memoryAdapter?.setOnClickListener(object : MemoriesAdapter.OnClickListener {
            override fun onClick(position: Int, model: Memory) {
                Toast.makeText(activity,"${memoryList[position].name} Clicked",Toast.LENGTH_SHORT).show()
            }
        })
    }
}