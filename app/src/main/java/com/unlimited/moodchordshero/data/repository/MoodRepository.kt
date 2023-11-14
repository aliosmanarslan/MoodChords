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

package com.unlimited.moodchordshero.data.repository

import androidx.room.Transaction
import com.unlimited.moodchordshero.data.dao.MoodDao
import com.unlimited.moodchordshero.data.models.mood.Mood
import com.unlimited.moodchordshero.util.extensions.calculateLastWeekMoods
import com.unlimited.moodchordshero.util.extensions.findMostCommonInMoodList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoodRepository @Inject constructor(private val moodDao: MoodDao) {

    @Transaction
    suspend fun insert(mood: Mood) {
        return moodDao.insert(mood)
    }

    suspend fun getWeeklyMood(): String {
        return findMostCommonInMoodList(
            calculateLastWeekMoods(moodDao.getAllMoodList())
        )
    }

    suspend fun getGeneralMood(): String {
        return findMostCommonInMoodList(moodDao.getAllMoodList())
    }

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: MoodRepository? = null

        fun getInstance(moodDao: MoodDao) =
            instance ?: synchronized(this) {
                instance ?: MoodRepository(moodDao).also { instance = it }
            }
    }
}