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
        web.loadUrl("https://www.momoshop.com.tw/goods/GoodsDetail.jsp?i_code=8154854&osm=Ad07&utm_source=googleshop&utm_medium=googleshop_USC&utm_content=bn&gclid=CjwKCAjwjuqDBhAGEiwAdX2cjx0wwJ1sUgbywYRjGFlP4L4FwUJwop05eQB1GR48SbEROW6l5DZwRBoC5PIQAvD_BwE")
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