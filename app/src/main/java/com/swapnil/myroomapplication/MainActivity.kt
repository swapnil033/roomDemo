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
    private lateinit var adapter: SubscriberAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory)[SubscriberViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SubscriberAdapter { selectedItem: Subscriber -> onItemClick(selectedItem) }
        binding.recyclerView.adapter = adapter
        displaySubscribersList()
    }

    private fun displaySubscribersList(){
        viewModel.subscribers.observe(this, Observer {
            Log.d("TAG", "displaySubscribersList: $it")
            adapter.updateList(it)
        })

        /*viewModel.message.observe(this, Observer{
            it.getContentIfNotHandled().let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })*/

        viewModel.messageS.observe(this, Observer{
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun onItemClick(subscriber: Subscriber){
        //Toast.makeText(this, "name ${subscriber.name}", Toast.LENGTH_LONG).show()

        viewModel.initUpdateDelete(subscriber)
    }
}