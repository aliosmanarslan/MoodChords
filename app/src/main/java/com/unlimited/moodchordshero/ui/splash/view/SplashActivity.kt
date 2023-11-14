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

package com.unlimited.moodchordshero.ui.splash.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.viewbinding.ViewBinding
import com.unlimited.moodchordshero.base.BaseActivity
import com.unlimited.moodchordshero.service.hms.HmsAuthService
import com.unlimited.moodchordshero.ui.home.view.HomeActivity
import com.unlimited.moodchordshero.ui.login.view.LoginActivity
import com.unlimited.moodchordshero.ui.network.view.NetworkCheckActivity
import com.unlimited.moodchordshero.ui.onboarding.view.OnboardingActivity
import com.unlimited.moodchordshero.ui.splash.viewmodel.SplashViewModel
import com.unlimited.moodchordshero.util.*
import com.unlimited.moodchordshero.util.SharedPreferencesHelper.hasUsername
import com.unlimited.moodchordshero.util.SharedPreferencesHelper.isFirstTime
import com.unlimited.moodchordshero.util.SharedPreferencesHelper.language
import java.util.*

class SplashActivity : BaseActivity<ViewBinding, SplashViewModel>() {

    override val viewModel: SplashViewModel? by viewModels()

    private lateinit var sharedPrefInfo: HashMap<String, Boolean>
    private var isLoadingFinished = MutableLiveData<Boolean>()

    override fun getViewBinding(): ViewBinding? {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val networkConnection = CheckInternetConnectionHelper(applicationContext)

        networkConnection.observe(this, {
            navigate(it)
        })

        sharedPrefInfo = getSharedPrefInfo()
        isLoadingFinished.value = true

        val prefs = SharedPreferencesHelper.customPreference(this, SHARED_PREFERENCES)
        val language = prefs.language
        if (!language.isNullOrBlank()) {
            LocaleHelper.setLocale(this, language)
        } else
            LocaleHelper.setLocale(this, Locale.getDefault().language)
    }

    private fun navigate(isNetworkConnected: Boolean) {
        if (isNetworkConnected) {
            val isFirstTime = sharedPrefInfo[DEF_FIRST_TIME] ?: true
            val hasUsername = sharedPrefInfo[DEF_HAS_USERNAME] ?: false
            checkFirstTimeLogin(isFirstTime, hasUsername)
        } else {
            startActivity(Intent(applicationContext, NetworkCheckActivity::class.java))
            finish()
        }
    }

    private fun getSharedPrefInfo(): HashMap<String, Boolean> {
        val prefs = SharedPreferencesHelper.customPreference(this, SHARED_PREFERENCES)
        val tempIsFirstTime = prefs.isFirstTime
        val tempHasUsername = prefs.hasUsername

        val result = hashMapOf<String, Boolean>()
        result[DEF_FIRST_TIME] = tempIsFirstTime
        result[DEF_HAS_USERNAME] = tempHasUsername

        Log.i(TAG, "getSharedPrefInfo() -> $result")
        return result
    }

    companion object {
        const val TAG = "SplashActivity TAG"
    }

    private fun checkFirstTimeLogin(isFirstTime: Boolean, hasUsername: Boolean) {
        if (isFirstTime) {
            startActivity(Intent(applicationContext, OnboardingActivity::class.java))
            finish()
        } else {
            checkSilentSignIn(hasUsername)
        }
    }

    private fun checkSilentSignIn(hasUsername: Boolean) {
        HmsAuthService.checkAuthWithSilentSignIn(this) { isSilentSignIn ->
            if (isSilentSignIn && hasUsername) {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
            } else {
                if (hasUsername) {
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                } else {
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                }
            }
            finish()
        }
    }

}