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

package com.unlimited.moodchordshero.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.unlimited.moodchordshero.util.ProgressDialog

abstract class BaseActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity() {

    var TAG = javaClass.simpleName

    var binding: VB? = null

    private var dialog: Dialog? = null

    protected abstract val viewModel: VM?

    protected abstract fun getViewBinding(): VB?

    protected fun hideLoading() {
        dialog?.let { dialog ->
            dialog.dismiss()
        }
    }

    protected fun showLoading(context: Context) {
        dialog = ProgressDialog.progressDialog(context)
        dialog?.show()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.i(TAG, "onCreate()")

        binding = getViewBinding()
        setContentView(binding?.root)
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy()")
    }

}