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

import android.app.Dialog
import android.content.Context
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.unlimited.moodchordshero.R

class ProgressDialog {

    companion object {

        fun progressDialog(context: Context): Dialog {
            val dialog = Dialog(context, R.style.ProgressDialogStyle)
            val layout = RelativeLayout(context)
            val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleLarge)
            val resource = context.resources
            val params = RelativeLayout.LayoutParams(
                resource.getDimensionPixelSize(R.dimen.progress_bar_size),
                resource.getDimensionPixelSize(R.dimen.progress_bar_size)
            )
            params.addRule(RelativeLayout.CENTER_IN_PARENT)
            layout.addView(progressBar, params)
            dialog.setContentView(layout)
            dialog.setCancelable(false)
            return dialog
        }

    }
}