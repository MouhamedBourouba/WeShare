package com.example.weshare.data.localdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weshare.domain.model.ArrayListTypeConverter
import com.example.weshare.domain.model.User

@Suppress("FORBIDDEN_SYNCHRONIZED_BY_VALUE_CLASSES_OR_PRIMITIVES")
@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ArrayListTypeConverter::class)
abstract class RoomDataBase : RoomDatabase() {

    abstract fun getUserDao(): UserDto

    companion object {
        @Volatile
        private var INSTANCE: RoomDataBase? = null
        fun getDataBase(context: Context): RoomDataBase {
            if (INSTANCE == null) {
                synchronized(true) {
                    INSTANCE =
                        Room.databaseBuilder(context, RoomDataBase::class.java, "WeShareLocalDB")
                            .build()
                    return INSTANCE!!
                }
            } else return INSTANCE!!
        }
    }
}