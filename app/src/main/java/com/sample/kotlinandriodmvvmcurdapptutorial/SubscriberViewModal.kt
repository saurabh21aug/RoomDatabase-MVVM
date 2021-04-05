package com.sample.kotlinandriodmvvmcurdapptutorial

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.kotlinandriodmvvmcurdapptutorial.db.SubscriberRepository
import com.sample.kotlinandriodmvvmcurdapptutorial.db.Subscriber
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SubscriberViewModal(private val repository: SubscriberRepository) : ViewModel(), Observable {


    val subscribers = repository.subscribers
    private var isUpdateOrDelete = false;
    private lateinit var subscribeToUpdateOrDelete: Subscriber

    @Bindable
    val inputName = MutableLiveData<String>()

    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()

    @Bindable
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {
        if (inputName.value == null) {
            _message.value = "Enter subscriber name"
            return
        } else if (inputName.value == null) {
            _message.value = "Enter subscriber email"
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            _message.value = "Enter correct email"
        } else {
            if (isUpdateOrDelete) {
                subscribeToUpdateOrDelete.name = inputName.value!!
                subscribeToUpdateOrDelete.email = inputEmail.value!!

                update(subscribeToUpdateOrDelete)

            } else {
                val name = inputName.value!!
                val email = inputEmail.value!!

                insert(Subscriber(0, name, email))
                inputName.value = null
                inputEmail.value = null
            }
        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete)
            delete(subscribeToUpdateOrDelete)
        else
            clearAll()
    }

    private fun insert(subscriber: Subscriber) =
        viewModelScope.launch {
            val newRowID = repository.insert(subscriber)
            if (newRowID > -1)
                _message.value = "Subscriber Inserted Successfully $newRowID"
            else
                _message.value = "Error Occurred"
        }

    private fun update(subscriber: Subscriber) =
        viewModelScope.launch {
            val newRowID = repository.update(subscriber)
            if (newRowID > -1) {
                inputName.value = null
                inputEmail.value = null
                isUpdateOrDelete = false
                saveOrUpdateButtonText.value = "Save"
                clearAllOrDeleteButtonText.value = "Clear All"
                _message.value = "$newRowID Row Updated Successfully"
            } else
                _message.value = "Error Occurred"
        }

    private fun delete(subscriber: Subscriber) =
        viewModelScope.launch {
            val noOfRowDeleted = repository.delete(subscriber)
            if (noOfRowDeleted > 0) {
                inputName.value = null
                inputEmail.value = null
                isUpdateOrDelete = false;
                saveOrUpdateButtonText.value = "Save"
                clearAllOrDeleteButtonText.value = "Clear All"
                _message.value = "$noOfRowDeleted Row  Deleted Successfully"
            } else {
                _message.value = "Error Occurred"
            }
        }

    private fun clearAll() = viewModelScope.launch {
        val noOfRowDeleted = repository.deleteAll()
        if (noOfRowDeleted > 0) {
            _message.value = "$noOfRowDeleted Subscriber  Deleted Successfully"
        } else {
            _message.value = "Error Occurred"

        }
    }

    fun initUpdateAndDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true;
        subscribeToUpdateOrDelete = subscriber
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

}