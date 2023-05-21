package com.zabi.travelmemories.ui.home


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.Places
import com.zabi.travelmemories.R
import com.zabi.travelmemories.adapters.MemoriesAdapter
import com.zabi.travelmemories.databinding.FragmentHomeBinding
import com.zabi.travelmemories.models.Location
import com.zabi.travelmemories.models.Memory
import com.zabi.travelmemories.utils.MemoryDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var memoryAdapter: MemoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        homeViewModel.memoryList.observe(viewLifecycleOwner) { memories ->
            if (memories.isEmpty()){
                binding.textHome.visibility = View.VISIBLE
            }
            else{
                binding.textHome.visibility = View.INVISIBLE
            }
            setupMemoryRecyclerView(memories as ArrayList<Memory>)
        }

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

        memoryAdapter = activity?.let { MemoriesAdapter(it,memoryList) }!!
        binding.rvHome.adapter = memoryAdapter


        memoryAdapter?.setOnClickListener(object : MemoriesAdapter.OnClickListener {
            override fun onClick(position: Int, model: Memory) {
                Toast.makeText(activity,"${memoryList[position].name} Clicked",Toast.LENGTH_SHORT).show()
            }
        })

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val memory = memoryList[position]

                if(direction == ItemTouchHelper.LEFT){
                    showDialog(memory, position)
                }else if(direction == ItemTouchHelper.RIGHT){
                    val action = HomeFragmentDirections.actionNavHomeToEditMemoryFragment(memory)
                    findNavController().navigate(action)
                }
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.rvHome)
    }

    private fun showDialog(memory: Memory, position: Int) {
        AlertDialog.Builder(context)
            .setTitle("Delete memory")
            .setMessage("Are you sure you want to delete this memory?")
            .setPositiveButton("Yes") { dialog, _ ->
                homeViewModel.deleteMemory(memory)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                memoryAdapter?.notifyItemChanged(position)
                dialog.dismiss()
            }
            .show()
    }
}