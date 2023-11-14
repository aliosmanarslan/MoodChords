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
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.unlimited.moodchordshero.R
import kotlinx.android.synthetic.main.toast_error_layout.view.*
import kotlinx.android.synthetic.main.toast_success_layout.view.*

class MoodChordsToast {

    companion object {
        private const val GRAVITY_BOTTOM = 400
        private lateinit var layoutInflater: LayoutInflater

        fun showErrorToast(context: Context?, message: String, duration: Int = Toast.LENGTH_SHORT) {
            layoutInflater = LayoutInflater.from(context)
            val layout = layoutInflater.inflate(R.layout.toast_error_layout, null)
            layout.tvToastErrorMessage.text = message
            val toast = Toast(context?.applicationContext)
            toast.duration = duration
            toast.setGravity(Gravity.BOTTOM,0, GRAVITY_BOTTOM)
            toast.view = layout
            toast.show()
        }


        fun showSuccessToast(context: Context?, message: String, duration: Int = Toast.LENGTH_SHORT) {
            layoutInflater = LayoutInflater.from(context)
            val layout = layoutInflater.inflate(R.layout.toast_success_layout, null)
            layout.tvToastSuccessMessage.text = message
            val toast = Toast(context?.applicationContext)
            toast.duration = duration
            toast.setGravity(Gravity.BOTTOM,0, GRAVITY_BOTTOM)
            toast.view = layout
            toast.show()
        }
    }
}