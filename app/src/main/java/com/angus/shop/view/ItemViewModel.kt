package com.angus.shop.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.angus.shop.model.Item
import com.angus.shop.view.FirebaseQueryLiveData

class ItemViewModel : ViewModel(){
    private var items = MutableLiveData<List<Item>>()
    private var firebaseQueryLiveData = FirebaseQueryLiveData()

    fun getItems() : FirebaseQueryLiveData {
        return firebaseQueryLiveData
    }

    fun getCategory(categoryId: String){
        firebaseQueryLiveData.setCategory(categoryId)
    }
}