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

package com.unlimited.moodchordshero.util

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.unlimited.moodchordshero.data.models.song.SongDataResponse

class JsonDataHelper {

    companion object {

        // for getting chords from json file with moshi lib.
        fun getChordsData(applicationContext: Context): List<SongDataResponse>? {

            val jsonFile: String =
                applicationContext.assets.open(CHORDS_DATA_FILENAME).bufferedReader()
                    .use { it.readText() }

            val adapter: JsonAdapter<List<SongDataResponse>> = Moshi.Builder()
                .build()
                .adapter(
                    Types.newParameterizedType(
                        MutableList::class.java,
                        SongDataResponse::class.java
                    )
                )

            return adapter.fromJson(jsonFile)
        }

    }
}