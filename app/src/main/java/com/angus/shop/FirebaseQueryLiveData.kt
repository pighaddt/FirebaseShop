package com.angus.shop

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*

class FirebaseQueryLiveData : LiveData<QuerySnapshot>(), EventListener<QuerySnapshot> {
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
            value = querySnapshot
        }
    }
}

