package com.mp.vocabulary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.mp.vocabulary.R
import com.mp.vocabulary.data.UserData
import com.mp.vocabulary.databinding.Row3Binding

class UserStudyAdapter(options: FirebaseRecyclerOptions<UserData>)
: FirebaseRecyclerAdapter<UserData, UserStudyAdapter.ViewHolder>(options) {
    inner class ViewHolder(val binding: Row3Binding): RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = Row3Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: UserData) {
        holder.binding.apply {
            studyDate.text = model.date
            studyNum.text = model.num.toString()
            when(model.num) {
                //mContext.getResources().getColor(R.color.yellow_200)
                in 0..5 -> tue.setBackgroundColor(ContextCompat.getColor(this.root.context, R.color.base_yellow))
                in 6..20 -> tue.setBackgroundColor(ContextCompat.getColor(this.root.context, R.color.yellow_200))
                in 21..40 -> tue.setBackgroundColor(ContextCompat.getColor(this.root.context, R.color.yellow_300))
                else -> tue.setBackgroundColor(ContextCompat.getColor(this.root.context, R.color.yellow_500))
            }
        } }
}