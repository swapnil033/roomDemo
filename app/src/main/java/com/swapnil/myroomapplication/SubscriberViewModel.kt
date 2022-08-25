package com.swapnil.myroomapplication

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swapnil.myroomapplication.db.Subscriber
import com.swapnil.myroomapplication.db.SubscriberRepository
import kotlinx.coroutines.launch
import java.util.regex.Pattern

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

        if(name.value == null){
            _message.value = Event("Please enter subscriber's name.");
        }else if(email.value == null){
            _message.value = Event("Please enter subscriber's email.");
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email.value!!).matches()){
            _message.value = Event("Please enter correct email address.");
        }else{
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
        val newAddedRow = repository.insert(subscriber)

        if(newAddedRow > -1)
            _message.value = Event("Subscriber added successfully!");
        else
            _message.value = Event("error occurred!");
    }


    private fun update(subscriber: Subscriber) = viewModelScope.launch {
        val updatedRow = repository.update(subscriber)
        if(updatedRow > -1)
            _message.value = Event("Subscriber updated successfully!");
        else
            _message.value = Event("error occurred!");
    }

    private fun delete(subscriber: Subscriber) = viewModelScope.launch {
        val noOfDeletedRow = repository.delete(subscriber)
        if(noOfDeletedRow > 0)
            _message.value = Event("Subscriber deleted successfully!");
        else
            _message.value = Event("error occurred!");
    }

    private fun deleteAll() = viewModelScope.launch {
        val noOfDeletedRow = repository.deleteAll()
        if(noOfDeletedRow > 0)
            _message.value = Event("all Subscribers deleted successfully!");
        else
            _message.value = Event("error occurred!");
    }

}