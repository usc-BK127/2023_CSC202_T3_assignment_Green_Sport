package com.example.greenspot.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.greenspot.model.GSpot

@Database(entities = [GSpot::class], exportSchema = false, version = 1)
abstract class GreenSpotDatabase: RoomDatabase() {
    abstract fun gSpotDao(): GreenSpotDao

    companion object{
        @Volatile
        private var INSTANCE: GreenSpotDatabase? = null

        fun getDatabase(context: Context): GreenSpotDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GreenSpotDatabase::class.java,
                    "gSpotDB"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}