package com.angus.shop

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = arrayOf(Item::class), version = 1, exportSchema = false)
abstract class ItemDatabase : RoomDatabase() {
    abstract fun getItemDao() : ItemDao
    companion object{
        private var instance : ItemDatabase? = null
        fun getInstance(context : Context) : ItemDatabase?{
            if (instance == null){
                instance = Room.databaseBuilder(context, ItemDatabase::class.java, "Item")
                    .allowMainThreadQueries() // room only work in main thread
                    .build()
            }
            return instance
        }
    }
}