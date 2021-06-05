package com.angus.shop.view

import androidx.lifecycle.LiveData
import com.angus.shop.model.Item
import com.google.firebase.firestore.*

class FirebaseQueryLiveData : LiveData<List<Item>>(), EventListener<QuerySnapshot> {
    private lateinit var registration: ListenerRegistration
    private var isRegistration : Boolean = false
    private var query = FirebaseFirestore.getInstance().collection("items")
    .orderBy("viewCount", Query.Direction.DESCENDING)
    .limit(10)

    override fun onActive() {
        super.onActive()
        registration =  query.addSnapshotListener(this)
        isRegistration = true
    }

    override fun onInactive() {
        super.onInactive()
        if (isRegistration){
            registration.remove()
        }
    }

    override fun onEvent(querySnapshot: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (querySnapshot != null  && !querySnapshot?.isEmpty){
            val list = mutableListOf<Item>()
            for (doc in querySnapshot.documents){
                val item = doc.toObject(Item::class.java) ?: Item()
                item.id = doc.id
                list.add(item)
            }
            value = list
        }
    }

    fun setCategory(categoryId: String) {
        if (isRegistration){
            registration.remove()
            isRegistration = false
        }
        if (categoryId.length > 0){
            query = FirebaseFirestore.getInstance().collection("items")
                .whereEqualTo("category", categoryId)
                .orderBy("viewCount", Query.Direction.DESCENDING)
                .limit(10)
        }else{
            query = FirebaseFirestore.getInstance().collection("items")
                .orderBy("viewCount", Query.Direction.DESCENDING)
                .limit(10)
        }
        registration = query.addSnapshotListener(this)
        isRegistration = true
    }
}

