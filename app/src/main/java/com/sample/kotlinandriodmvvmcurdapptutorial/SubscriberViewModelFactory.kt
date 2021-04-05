package com.sample.kotlinandriodmvvmcurdapptutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.kotlinandriodmvvmcurdapptutorial.db.SubscriberRepository
import java.lang.IllegalArgumentException


class SubscriberViewModelFactory(private val repository: SubscriberRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubscriberViewModal::class.java)) {
            return SubscriberViewModal(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}