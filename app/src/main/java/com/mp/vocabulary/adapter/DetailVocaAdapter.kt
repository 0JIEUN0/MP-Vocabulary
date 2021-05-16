package com.mp.vocabulary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.mp.vocabulary.R
import com.mp.vocabulary.data.SimpleVoca
import com.mp.vocabulary.data.Voca
import com.mp.vocabulary.databinding.Row1Binding
import com.mp.vocabulary.databinding.Row2Binding

class DetailVocaAdapter(
        val context: Context,
        var items: MutableList<Voca>):
    RecyclerView.Adapter<DetailVocaAdapter.ViewHolder>() {

    var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClickSound(holder: RecyclerView.ViewHolder, view: View, data: Voca, position: Int)
        fun onClickStar(holder: RecyclerView.ViewHolder, view: View, data: Voca, position: Int)
    }

    inner class ViewHolder(val binding: Row2Binding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.soundBtn.setOnClickListener{
                itemClickListener?.onClickSound(
                        this, it, items[adapterPosition], adapterPosition
                )
            }
            binding.engText.setOnClickListener {
                itemClickListener?.onClickSound(
                        this, it, items[adapterPosition], adapterPosition
                )
            }
            binding.starBtn.setOnClickListener {
                itemClickListener?.onClickStar(
                        this, it, items[adapterPosition], adapterPosition
                )
            }
        }
    }

    fun moveItem(oldPos: Int, newPos: Int) {
        val item = items[oldPos]
        items.removeAt(oldPos)
        items.add(newPos, item)
        notifyItemMoved(oldPos, newPos)
    }

    fun removeItem(pos: Int) {
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun addItem(newVoca: Voca) = items.add(newVoca)
    fun setNewItems(newList: MutableList<Voca>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailVocaAdapter.ViewHolder {
        val binding: Row2Binding = Row2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailVocaAdapter.ViewHolder, position: Int) {
        holder.binding.engText.text = items[position].eng
        // make chips
        items[position].kor.forEach {
            val chip: Chip = Chip(context)
            chip.text = it
            chip.textSize = 13.0f
            holder.binding.rowChipGroup.addView(chip)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}