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

package com.unlimited.moodchordshero.data.dao

import androidx.room.*
import com.unlimited.moodchordshero.data.models.mood.Mood

@Dao
interface MoodDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mood: Mood?)

    @Query("SELECT * FROM mood ORDER BY mood ASC")
    suspend fun getAllMoodList() : List<Mood>?

    @Query("SELECT * FROM mood WHERE savedDate BETWEEN datetime('now', '-6 days') AND datetime('now', 'localtime')")
    suspend fun getLastWeekRecords(): List<Mood>?
}