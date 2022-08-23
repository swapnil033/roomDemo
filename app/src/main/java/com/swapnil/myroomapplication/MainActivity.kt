package com.swapnil.myroomapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.swapnil.myroomapplication.adapter.SubscriberAdapter
import com.swapnil.myroomapplication.databinding.ActivityMainBinding
import com.swapnil.myroomapplication.db.Subscriber
import com.swapnil.myroomapplication.db.SubscriberDatabase
import com.swapnil.myroomapplication.db.SubscriberRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SubscriberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[SubscriberViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        displaySubscribersList()
    }

    private fun displaySubscribersList(){
        viewModel.subscribers.observe(this, Observer {
            Log.d("TAG", "displaySubscribersList: $it")
            binding.recyclerView.adapter = SubscriberAdapter(it) { selectedItem: Subscriber ->
                onItemClick(selectedItem)
            }
        })
    }

    private fun onItemClick(subscriber: Subscriber){
        Toast.makeText(this, "name ${subscriber.name}", Toast.LENGTH_LONG).show()
    }
}