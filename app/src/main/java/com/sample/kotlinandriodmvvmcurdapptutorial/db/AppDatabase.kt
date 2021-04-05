package com.sample.kotlinandriodmvvmcurdapptutorial.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Subscriber::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun subscriberDAO(): SubscriberDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "subscriber_data_database"
                    )
                       // .fallbackToDestructiveMigration() // this use use when you want add new version(remove all added new db) of data base, but make sure  tag added in manifest file android:allowBackup="false"
                        .build()
                }
                return instance
            }
        }

    }
}
