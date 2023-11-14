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

package com.unlimited.moodchordshero.ui.chords.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.unlimited.moodchordshero.base.BaseViewModel
import com.unlimited.moodchordshero.data.models.mood.Mood
import com.unlimited.moodchordshero.data.models.song.Song
import com.unlimited.moodchordshero.data.repository.MoodRepository
import com.unlimited.moodchordshero.data.repository.SongRepository
import com.unlimited.moodchordshero.util.extensions.getCurrentDate
import kotlinx.coroutines.launch

class ChordsViewModel @ViewModelInject constructor(
    private val songRepository: SongRepository,
    private val moodRepository: MoodRepository
) :
    BaseViewModel() {

    val emotionArgs = MutableLiveData<String>()
    val newEmotionArgs = MutableLiveData<String>()

    val songArgs = MutableLiveData<Song>()
    val song = MutableLiveData<Song>()

    fun addToFavList(song: Song) {
        viewModelScope.launch {
            songRepository.updateSong(song)
        }
    }

    fun getSongByEmotion(emotionData: String) {
        viewModelScope.launch {
            song?.value =
                songRepository.getSongByEmotionResult(emotionData)?.shuffled()?.take(1)?.get(0)
        }
    }

    fun insertMood(emotion: String) {
        viewModelScope.launch { moodRepository.insert(Mood(emotion, getCurrentDate())) }
    }

    fun checkSong(songId: Long, newSongId: Long): Boolean {
        if (songId == newSongId) {
            return false
        }
        return true
    }

}