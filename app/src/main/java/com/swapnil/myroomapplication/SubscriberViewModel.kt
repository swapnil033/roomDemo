package com.swapnil.myroomapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swapnil.myroomapplication.db.Subscriber
import com.swapnil.myroomapplication.db.SubscriberRepository
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel() {

    private var isUpdateOrDelete = false
    private lateinit var subscriberIsUpdateOrDelete : Subscriber

    val subscribers = repository.subscribers

    val name = MutableLiveData<String?>()

    val email = MutableLiveData<String?>()

    val saveOrUpdateButtonTxt = MutableLiveData<String>()

    val clearOrDeleteButtonTxt = MutableLiveData<String>()

    private val _message = MutableLiveData<Event<String>>()

    val message : LiveData<Event<String>>
    get() = _message

    init {
        saveOrUpdateButtonTxt.value = "Save"

        clearOrDeleteButtonTxt.value = "Clear All"
    }

    fun saveOrUpdate(){
        if(isUpdateOrDelete){
            subscriberIsUpdateOrDelete.name = name.value!!
            subscriberIsUpdateOrDelete.email = email.value!!
            update(subscriberIsUpdateOrDelete)
        }else{
            insert(Subscriber(0, name.value!!, email.value!!))
        }

        saveOrUpdateButtonTxt.value = "Save"
        clearOrDeleteButtonTxt.value = "Clear All"
        isUpdateOrDelete = false
        name.value = null
        email.value = null
    }

    fun clearOrDelete(){

        if(isUpdateOrDelete){
            delete(subscriberIsUpdateOrDelete)
        }else{
            deleteAll()
        }

        saveOrUpdateButtonTxt.value = "Save"
        clearOrDeleteButtonTxt.value = "Clear All"
        isUpdateOrDelete = false
        name.value = null
        email.value = null
    }

    fun initUpdateDelete(subscriber: Subscriber){
        name.value = subscriber.name
        email.value = subscriber.email

        isUpdateOrDelete = true
        subscriberIsUpdateOrDelete = subscriber
        saveOrUpdateButtonTxt.value = "Update"

        clearOrDeleteButtonTxt.value = "Delete"
    }

    private fun insert(subscriber: Subscriber) = viewModelScope.launch {
        repository.insert(subscriber)
        _message.value = Event("Subscriber added successfully!");
    }


    private fun update(subscriber: Subscriber) = viewModelScope.launch {
        repository.update(subscriber)
        _message.value = Event("Subscriber updated successfully!");
    }

    private fun delete(subscriber: Subscriber) = viewModelScope.launch {
        repository.delete(subscriber)
        _message.value = Event("Subscriber deleted successfully!");
    }

    private fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
        _message.value = Event("all Subscribers deleted successfully!");
    }

}