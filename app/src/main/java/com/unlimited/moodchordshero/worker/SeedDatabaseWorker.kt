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

package com.unlimited.moodchordshero.worker

import android.content.Context
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.unlimited.moodchordshero.data.database.AppDatabase
import com.unlimited.moodchordshero.data.models.song.SongDataResponse
import com.unlimited.moodchordshero.data.repository.SongRepository
import com.unlimited.moodchordshero.util.JsonDataHelper
import kotlinx.coroutines.coroutineScope

class SeedDatabaseWorker @WorkerInject constructor (
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val TAG = "SeedDatabaseWorker"
    }

    override suspend fun doWork(): Result = coroutineScope {
        try {
            Log.i(TAG, "Seeding db with SeedDatabaseWorker")
            JsonDataHelper.getChordsData(applicationContext)
                ?.let { songDataResponseList: List<SongDataResponse> ->

                    val database = AppDatabase.getInstance(applicationContext)
                    val songRepository = SongRepository.getInstance(database.songDao())

                    for (songDataResponse in songDataResponseList) {
                        songRepository.insert(songDataResponse.toSong())
                    }

                    Result.success()

                } ?: throw Exception()


        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

}