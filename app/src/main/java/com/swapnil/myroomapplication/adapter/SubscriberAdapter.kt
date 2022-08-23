package com.swapnil.myroomapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swapnil.myroomapplication.databinding.RowSubscriberBinding
import com.swapnil.myroomapplication.db.Subscriber

class SubscriberAdapter(
    private val list : List<Subscriber>,
    private val clickListener : (Subscriber) -> Unit
) : RecyclerView.Adapter<SubscriberHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriberHolder {
        val binding = RowSubscriberBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SubscriberHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriberHolder, position: Int) {
        holder.binding.model = list[position]
        holder.itemView.setOnClickListener {
            clickListener(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}

class SubscriberHolder(val binding: RowSubscriberBinding) : RecyclerView.ViewHolder(binding.root) {
}