package com.angus.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

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