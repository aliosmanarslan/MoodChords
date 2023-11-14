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

package com.unlimited.moodchordshero.ui.profile.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.unlimited.moodchordshero.base.BaseViewModel
import com.unlimited.moodchordshero.data.repository.MoodRepository
import kotlinx.coroutines.launch

class ProfileViewModel @ViewModelInject constructor(
    private val moodRepository: MoodRepository
) : BaseViewModel() {

    val generalMoodMutableLiveData: MutableLiveData<String>? = MutableLiveData<String>()

    fun getGeneralMood(){
        viewModelScope.launch { generalMoodMutableLiveData?.value =moodRepository.getGeneralMood() }
    }
}