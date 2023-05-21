package com.zabi.travelmemories.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.zabi.travelmemories.databinding.MemoryCardBinding
import com.zabi.travelmemories.models.Memory
import com.zabi.travelmemories.ui.home.HomeFragmentDirections

class MemoriesAdapter(private val context: Context, private var list: ArrayList<Memory>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    class ViewHolder(binding: MemoryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivMemory = binding.ivMemoryCard
        val tvMemoryName = binding.tvMemoryName
        val tvMemoryPlace = binding.tvMemoryPlace
        val tvMemoryDate = binding.tvMemoryDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            MemoryCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val memory = list[position]

        if (holder is ViewHolder) {
            holder.ivMemory.setImageURI(Uri.parse(memory.image))
            holder.tvMemoryName.text = memory.name
            holder.tvMemoryPlace.text = memory.place
            holder.tvMemoryDate.text = memory.date

            holder.itemView.setOnClickListener {
                val action = HomeFragmentDirections.actionNavHomeToDetailsFragment(memory)
                it.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickListener {
        fun onClick(position: Int, model: Memory)
    }

}