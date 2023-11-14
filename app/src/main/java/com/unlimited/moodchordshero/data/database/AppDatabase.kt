/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.unlimited.moodchordshero.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.unlimited.moodchordshero.data.dao.MoodDao
import com.unlimited.moodchordshero.data.dao.SongDao
import com.unlimited.moodchordshero.data.models.mood.Mood
import com.unlimited.moodchordshero.data.models.song.Song
import com.unlimited.moodchordshero.util.DATABASE_NAME
import com.unlimited.moodchordshero.util.DateConverter
import com.unlimited.moodchordshero.worker.SeedDatabaseWorker

@Database(
    entities = [Song::class, Mood::class],
    version = AppDatabase.VERSION,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao
    abstract fun moodDao(): MoodDao

    companion object {
        const val VERSION = 1

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                //.allowMainThreadQueries() // remove this after starting to use view models
                .build()
        }
    }

}


