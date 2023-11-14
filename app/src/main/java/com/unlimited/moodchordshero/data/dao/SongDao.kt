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
import com.unlimited.moodchordshero.data.models.song.Song

@Dao
interface SongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(songList: List<Song>?)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(song: Song?)

    @Update
    suspend fun updateSong(vararg song: Song)

    @Query("SELECT * FROM song ORDER BY songObjectId ASC")
    suspend fun getAllSongList(): List<Song>

    @Query("SELECT * FROM song WHERE songTitle = :songTitle ORDER BY songTitle ASC")
    fun getChordsBySongTitle(songTitle: String): List<Song>

    @Query("SELECT * FROM song WHERE songObjectId = :id")
    fun getChordsBySongId(id: Long): List<Song>

    @Query("SELECT * FROM song WHERE singer = :singer ORDER BY singer ASC")
    fun getChordsBySinger(singer: String): List<Song>

    @Query("SELECT * FROM song WHERE isFavorite = 1 ORDER BY songObjectId ASC")
    suspend fun getFavChords(): List<Song>

    @Query("SELECT * FROM song WHERE emotionCategory LIKE :emotionResult")
    suspend fun getSongByEmotionResult(emotionResult: String): List<Song>

}