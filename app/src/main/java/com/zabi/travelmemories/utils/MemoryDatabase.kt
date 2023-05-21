package com.zabi.travelmemories.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zabi.travelmemories.models.Memory


@Database(entities = [Memory::class], version = 1, exportSchema = false)
@TypeConverters(Convertors::class)
abstract class MemoryDatabase : RoomDatabase() {

    abstract val memoryDao: MemoryDAO

    companion object {
        @Volatile
        private var INSTANCE: MemoryDatabase? = null

        fun getInstance(context: Context): MemoryDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MemoryDatabase::class.java,
                        "memory_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}