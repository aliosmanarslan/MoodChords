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

package com.unlimited.moodchordshero.di

import android.content.Context
import androidx.room.Room
import com.unlimited.moodchordshero.data.dao.MoodDao
import com.unlimited.moodchordshero.data.dao.SongDao
import com.unlimited.moodchordshero.data.database.AppDatabase
import com.unlimited.moodchordshero.util.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class RoomDBModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideSongDao(appDatabase: AppDatabase): SongDao {
        return appDatabase.songDao()
    }

    @Provides
    fun provideMoodDao(appDatabase: AppDatabase): MoodDao {
        return appDatabase.moodDao()
    }
}