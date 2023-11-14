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

package com.unlimited.moodchordshero.ui.home.viewmodel

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

class HomeViewModel @ViewModelInject constructor(
    private val songRepository: SongRepository,
    private val moodRepository: MoodRepository
) :
    BaseViewModel() {

    val favListMutableLiveData: MutableLiveData<List<Song>>? = MutableLiveData<List<Song>>()
    val allSongListMutableLiveData: MutableLiveData<List<Song>>? = MutableLiveData<List<Song>>()
    val weeklyMoodMutableLiveData: MutableLiveData<String>? = MutableLiveData<String>()

    fun getFavList() {
        viewModelScope.launch {
            favListMutableLiveData?.value = songRepository.getFavList()
        }
    }

    fun getAllSongList() {
        viewModelScope.launch {
            allSongListMutableLiveData?.value = songRepository.getAllSongList()
        }
    }

    fun insertMood(emotion: String){
        viewModelScope.launch { moodRepository.insert(Mood(emotion, getCurrentDate())) }
    }

    fun getWeeklyMood(){
        viewModelScope.launch { weeklyMoodMutableLiveData?.value =moodRepository.getWeeklyMood() }
    }
}