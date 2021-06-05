package com.angus.shop.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.angus.shop.data.ItemRepository
import com.angus.shop.model.Item
import com.angus.shop.view.FirebaseQueryLiveData

class ItemViewModel(application: Application) : AndroidViewModel(application){
    private lateinit var itemRepository: ItemRepository
    init {
        itemRepository = ItemRepository(application)
    }

    fun getItems() : LiveData<List<Item>> {
        return itemRepository.getAllItems()
    }

    fun getCategory(categoryId: String){
        itemRepository.setCategory(categoryId)
    }
}