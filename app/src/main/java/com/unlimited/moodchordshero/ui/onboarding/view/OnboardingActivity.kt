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

package com.unlimited.moodchordshero.ui.onboarding.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.unlimited.moodchordshero.R
import com.unlimited.moodchordshero.worker.SeedDatabaseWorker
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnboardingActivity  : AppCompatActivity() {
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        setSupportActionBar(toolbar)

        //Setting up NavController and NavHostFragment
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.onboardingViewPagerFragment) {
                //Hide toolbar in the landing screen
                toolbar.visibility = View.GONE
            } else {
                //Show toolbar in all other cases
                toolbar.visibility = View.VISIBLE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        enqueueWorker(this)
    }

    private fun enqueueWorker(context: Context) {
        val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}