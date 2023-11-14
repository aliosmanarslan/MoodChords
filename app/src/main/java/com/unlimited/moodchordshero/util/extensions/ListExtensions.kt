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

package com.unlimited.moodchordshero.util.extensions

import com.unlimited.moodchordshero.data.models.mood.Mood
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

fun findMostCommonInMoodList(allMoodList: List<Mood>?): String {
    val result = allMoodList?. let { allMoodList?.groupingBy { it.mood }.eachCount().toList().sortedByDescending{ it.second }.take(
        1
    ).toMap() }
    return (result?.keys.toString()).replace("[", "").replace("]", "")
}

fun calculateLastWeekMoods(lastWeekRecords: List<Mood>?): List<Mood> {
    val resultList: MutableList<Mood> = ArrayList()
    if (lastWeekRecords != null) {
        for (mood in lastWeekRecords) {
            if (TimeUnit.MILLISECONDS.toDays(getCurrentDate().time - mood.savedDate?.time!!) <= 7.toLong()) {
                resultList.add(mood)
            }
        }
    }
    return resultList
}