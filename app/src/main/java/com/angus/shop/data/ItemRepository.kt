package com.angus.shop.data

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import com.angus.shop.model.Item
import com.angus.shop.view.FirebaseQueryLiveData
import java.lang.Appendable

class ItemRepository (application: Application){
    private var itemDao : ItemDao
    private lateinit var items : LiveData<List<Item>>
    private var firebaseQueryLiveData =  FirebaseQueryLiveData()
    private var  network = false
    init {
        itemDao = ItemDatabase.getInstance(application).getItemDao()
        items = itemDao.getItems()
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo  = cm.activeNetworkInfo
        network = networkInfo!!.isConnected
    }
    fun getAllItems() : LiveData<List<Item>>{
        if (network){
            //offLine
            items = itemDao.getItems()
        }else{
            //onLine
        items = firebaseQueryLiveData
        }

      return items
    }

    fun setCategory(categoryId: String) {
        if(network){
            firebaseQueryLiveData.setCategory(categoryId)
        }else{
            items = itemDao.getItemByCategory(categoryId)
        }
    }
}