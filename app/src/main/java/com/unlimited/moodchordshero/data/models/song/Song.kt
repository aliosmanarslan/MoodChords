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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Song(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "songObjectId")
    var songObjectId: Long,

    @ColumnInfo(name = "songTitle")
    val songTitle: String,

    @ColumnInfo(name = "singer")
    val singer: String?,

    @ColumnInfo(name = "emotionCategory")
    val emotionCategory: String?,

    @ColumnInfo(name = "coverUrl")
    val coverUrl: String?,

    @ColumnInfo(name = "chordsLink")
    val chordsLink: String,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean
) : Serializable