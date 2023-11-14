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
import com.unlimited.moodchordshero.data.dao.SongDao
import com.unlimited.moodchordshero.data.models.song.Song
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongRepository @Inject constructor(private val songDao: SongDao) {

    @Transaction
    suspend fun insert(song: Song) {
        return songDao.insert(song)
    }

    fun insertAllSongs(songList: List<Song>?) {
        return songDao.insertAll(songList)
    }

    fun getChordsBySinger(singer: String): List<Song> {
        return songDao.getChordsBySinger(singer)
    }

    fun getChordsBySongTitle(songTitle: String): List<Song> {
        return songDao.getChordsBySongTitle(songTitle)
    }

    fun getChordsBySongId(songId: Long): List<Song> {
        return songDao.getChordsBySongId(songId)
    }

    suspend fun updateSong(song: Song) {
        return songDao.updateSong(song)
    }

    suspend fun getFavList(): List<Song> {
        return songDao.getFavChords()
    }

    suspend fun getAllSongList(): List<Song> {
        return songDao.getAllSongList()
    }

    suspend fun getSongByEmotionResult(emotionResult: String): List<Song> {
        return songDao.getSongByEmotionResult(emotionResult)
    }

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: SongRepository? = null

        fun getInstance(songDao: SongDao) =
            instance ?: synchronized(this) {
                instance ?: SongRepository(songDao).also { instance = it }
            }
    }
}