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
import android.content.SharedPreferences

object SharedPreferencesHelper {

    fun customPreference(context: Context, name: String): SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    private inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.isFirstTime
        get() = getBoolean(IS_FIRST_TIME, true)
        set(value) {
            editMe {
                it.putBoolean(IS_FIRST_TIME, value)
            }
        }

    val SharedPreferences.hasUsername: Boolean
        get() = getString(USERNAME, null)?.let { true } ?: false


    var SharedPreferences.username
        get() = getString(USERNAME, null)
        set(value) {
            editMe {
                it.putString(USERNAME, value)
            }
        }

    var SharedPreferences.language
        get() = getString(LANGUAGE, null)
        set(value) {
            editMe {
                it.putString(LANGUAGE, value)
            }
        }

    var SharedPreferences.profilePicture
        get() = getString(PROFILE_PICTURE, null)
        set(value) {
            editMe {
                it.putString(PROFILE_PICTURE, value)
            }
        }


    fun SharedPreferences.clearValues() {
        editMe {
            val lan = this.language
            it.clear()
            it.putBoolean(IS_FIRST_TIME, false)
            it.putString(LANGUAGE, lan)
        }
    }

}