package com.angus.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    lateinit var item: Item

    companion object{
        private val TAG = DetailActivity::class.java.simpleName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        item = intent.getParcelableExtra<Item>("ITEM")!!
        web.settings.javaScriptEnabled = true
        web.loadUrl("https://translate.google.com.tw/?hl=zh-TW&sl=en&tl=zh-TW&text=extraordinary&op=translate")
//        Log.d(TAG, "onCreate: ${item.content}")
    }

    override fun onStart() {
        super.onStart()
        item.viewCount++
        item.id?.let {
            FirebaseFirestore.getInstance().collection("items")
                .document(it).update("viewCount", item.viewCount)

        }
    }
}