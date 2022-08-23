package com.swapnil.myroomapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swapnil.myroomapplication.db.Subscriber
import com.swapnil.myroomapplication.db.SubscriberRepository
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel() {

    val subscribers = repository.subscribers

    val name = MutableLiveData<String?>()

    val email = MutableLiveData<String?>()

    val saveOrUpdateButtonTxt = MutableLiveData<String>()

    val clearOrDeleteButtonTxt = MutableLiveData<String>()

    init {
        saveOrUpdateButtonTxt.value = "Save"

        clearOrDeleteButtonTxt.value = "Clear All"
    }

    fun saveOrUpdate(){
        insert(Subscriber(0, name.value!!, email.value!!))
        name.value = null
        email.value = null
    }

    fun clearOrDelete(){
        deleteAll()
    }

    fun insert(subscriber: Subscriber) = viewModelScope.launch {
        repository.insert(subscriber)
    }


    fun update(subscriber: Subscriber) = viewModelScope.launch {
        repository.update(subscriber)
    }

    fun delete(subscriber: Subscriber) = viewModelScope.launch {
        repository.delete(subscriber)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

}