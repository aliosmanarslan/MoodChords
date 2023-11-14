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

package com.unlimited.moodchordshero.ui.network.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.unlimited.moodchordshero.R
import com.unlimited.moodchordshero.ui.splash.view.SplashActivity
import com.unlimited.moodchordshero.util.CheckInternetConnectionHelper
import com.unlimited.moodchordshero.util.MoodChordsToast

class NetworkCheckActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_check)

        val networkConnection = CheckInternetConnectionHelper(applicationContext)
        networkConnection.observe(this, Observer{ isConnected ->
            if (isConnected) {
                MoodChordsToast.showSuccessToast(
                    this,
                    resources.getString(R.string.successInternetConnection),
                    Toast.LENGTH_SHORT
                )
                startActivity(Intent(applicationContext, SplashActivity::class.java))
                finish()

            } else {
                MoodChordsToast.showErrorToast(
                    this,
                    resources.getString(R.string.errorInternetConnection),
                    Toast.LENGTH_SHORT
                )
            }
        })
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}
