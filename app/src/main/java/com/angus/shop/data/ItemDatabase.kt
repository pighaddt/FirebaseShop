package com.angus.shop.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.angus.shop.model.Item

@Database(entities = arrayOf(Item::class), version = 1, exportSchema = false)
abstract class ItemDatabase : RoomDatabase() {
    abstract fun getItemDao() : ItemDao
    companion object{
        private lateinit var context: Context
        private val database : ItemDatabase by lazy {
            Room.databaseBuilder(context, ItemDatabase::class.java, "Item")
                .allowMainThreadQueries() // room only work in main thread
                .build()
        }
        fun getInstance(context : Context) : ItemDatabase{
           Companion.context = context
            return database
        }
    }
}