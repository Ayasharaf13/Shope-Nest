package com.example.shopenest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.shopenest.model.CustomerAddress
import com.example.shopenest.model.DraftOrderHeaderEntity
import com.example.shopenest.model.LineItem

import com.example.shopenest.model.Product
import com.example.shopenest.utilities.Converter

@Database(
    entities = [Product::class, DraftOrderHeaderEntity::class, LineItem::class],
    version = 18,
    exportSchema = true
)

@TypeConverters(Converter::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getProdDao(): ShoppingDao


    companion object {

        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(ctx: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    AppDataBase::class.java,
                    "Shopping_db"
                )

                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }


    }

}