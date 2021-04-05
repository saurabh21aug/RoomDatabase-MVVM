package com.sample.kotlinandriodmvvmcurdapptutorial

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.kotlinandriodmvvmcurdapptutorial.databinding.ActivityMainBinding
import com.sample.kotlinandriodmvvmcurdapptutorial.db.AppDatabase
import com.sample.kotlinandriodmvvmcurdapptutorial.db.Subscriber
import com.sample.kotlinandriodmvvmcurdapptutorial.db.SubscriberRepository

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var subscriberViewModal: SubscriberViewModal
    lateinit var adapter: MyRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dao = AppDatabase.getInstance(application).subscriberDAO()
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)
        subscriberViewModal =
            ViewModelProvider(this, factory).get(SubscriberViewModal::class.java)

        binding.subscriberViewModal = subscriberViewModal

        binding.lifecycleOwner = this

        initRecyclerView()

        subscriberViewModal.message.observe(this, {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun initRecyclerView() {
        binding.subscriberRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyRecyclerViewAdapter { item: Subscriber -> listItemClick(item) }
        binding.subscriberRecyclerView.adapter = adapter
        displaySubscriberList()

    }

    private fun displaySubscriberList() {
        subscriberViewModal.subscribers.observe(this, {
            Log.i("TAG", "displaySubscriberList: $it")
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClick(subscriber: Subscriber) {
        subscriberViewModal.initUpdateAndDelete(subscriber)
    }
}