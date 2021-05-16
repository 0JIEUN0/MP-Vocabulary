package com.mp.vocabulary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mp.vocabulary.data.SimpleVoca
import com.mp.vocabulary.databinding.Row1Binding

class SimpleVocaAdapter(var items: ArrayList<SimpleVoca>):
RecyclerView.Adapter<SimpleVocaAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: Row1Binding): RecyclerView.ViewHolder(binding.root){
    }

    fun addItem(eng: String, kor: String, color: Int) {
        items.add(SimpleVoca(eng, kor, color))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: Row1Binding = Row1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.eng.text = items[position].eng
        holder.binding.kor.text = items[position].kor
        holder.binding.kor.setTextColor(items[position].color)
    }
    override fun getItemCount(): Int {
        return items.size
    }
}
