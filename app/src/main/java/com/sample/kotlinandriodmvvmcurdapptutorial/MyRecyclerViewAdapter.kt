package com.sample.kotlinandriodmvvmcurdapptutorial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sample.kotlinandriodmvvmcurdapptutorial.databinding.ListItemBinding
import com.sample.kotlinandriodmvvmcurdapptutorial.db.Subscriber

class MyRecyclerViewAdapter(
    private val clickListener: (Subscriber) -> Unit
) : RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>() {

    private val subscribers = ArrayList<Subscriber>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(subscribers[position], clickListener)
    }

    override fun getItemCount() = subscribers.size

    fun setList(subscribers: List<Subscriber>) {
        this.subscribers.clear()
        this.subscribers.addAll(subscribers)

    }

    inner class MyViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subscriber: Subscriber, clickListener: (Subscriber) -> Unit) {
            binding.nameTextView.text = subscriber.name
            binding.emailTextView.text = subscriber.email
            binding.listItemLayout.setOnClickListener {
                clickListener(subscriber)
            }
        }
    }

}