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

package com.unlimited.moodchordshero.data.models.song

import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SongDataResponse(

    @PrimaryKey(autoGenerate = true)
    @field:Json(name = "songObjectId")
    var songObjectId: Long,

    @field:Json(name = "songTitle")
    val songTitle: String,

    @field:Json(name = "singer")
    val singer: String,

    @field:Json(name = "emotionCategory")
    val emotionCategory: String,

    @field:Json(name = "coverUrl")
    val coverUrl: String,

    @field:Json(name = "chordsLink")
    val chordsLink: String,
) {
    fun toSong(): Song {
        return Song(
            songObjectId = songObjectId,
            songTitle = songTitle,
            singer = singer,
            emotionCategory = emotionCategory,
            coverUrl = coverUrl,
            chordsLink = chordsLink,
            isFavorite = false
        )
    }
}