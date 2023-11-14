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

package com.unlimited.moodchordshero.ui.login.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.observe
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService
import com.unlimited.moodchordshero.R
import com.unlimited.moodchordshero.base.BaseActivity
import com.unlimited.moodchordshero.ui.home.view.HomeActivity
import com.unlimited.moodchordshero.ui.login.viewmodel.LoginViewModel
import com.unlimited.moodchordshero.util.*
import com.unlimited.moodchordshero.util.SharedPreferencesHelper.profilePicture
import com.unlimited.moodchordshero.util.SharedPreferencesHelper.username
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<ViewBinding, LoginViewModel>() {

    override val viewModel: LoginViewModel? by viewModels()

    private var networkStatus: Boolean = false

    override fun getViewBinding(): ViewBinding? {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        button_signIn_huawei_id.setOnClickListener {
            startAuthorization()
            showLoading(this)
        }

        observeLogin()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        networkStatus = CheckInternetConnectionHelper.checkInternetStatus(this)

        if (!networkStatus) {
            MoodChordsToast.showErrorToast(
                this,
                resources.getString(R.string.errorInternetConnection),
                Toast.LENGTH_LONG
            )
            hideLoading()
            return
        }

        if (requestCode == HUAWEI_AUTH_CODE) {
            checkAuthorization(data) {
                if (it && imageView_user_picture.drawable != null) {
                    viewModel!!.isSuccess.value = true
                }
            }
        }
    }

    private fun startAuthorization() {
        val authParams: HuaweiIdAuthParams =
            HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setAuthorizationCode()
                .setProfile()
                .createParams()
        val service: HuaweiIdAuthService =
            HuaweiIdAuthManager.getService(this, authParams)
        startActivityForResult(service.signInIntent, HUAWEI_AUTH_CODE)
    }

    private fun checkAuthorization(data: Intent?, callback: (Boolean) -> Unit) {
        val authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data)
        if (authHuaweiIdTask.isSuccessful) {
            val huaweiAccount = authHuaweiIdTask.result
            huaweiAccount.let {
                textView_username.text = (huaweiAccount.displayName)
                val transformation = MultiTransformation(CenterCrop(), RoundedCorners(45))
                if (huaweiAccount.avatarUriString != "") {
                    Glide.with(this)
                        .asBitmap()
                        .load(huaweiAccount.avatarUriString)
                        .transform(transformation)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                imageView_user_picture.setImageBitmap(resource)
                                callback(true)
                            }
                            override fun onLoadCleared(placeholder: Drawable?) {
                                // this is called when imageView is cleared on lifecycle call or for
                                // some other reason.
                                // if you are referencing the bitmap somewhere else too other than this imageView
                                // clear it here as you can no longer have the bitmap
                            }
                        })
                }

                hideLoading()
            }
        } else {
            // The sign-in failed.
            callback(false)
            Log.e(
                TAG,
                "sign in failed : " + (authHuaweiIdTask.exception as ApiException).statusCode
            )
        }
    }

    private fun observeLogin() {
        viewModel!!.isSuccess.observe(this) {
            if (it) {
                val username = textView_username.text.toString()

                val sharedPreferences =
                    SharedPreferencesHelper.customPreference(this, SHARED_PREFERENCES)
                sharedPreferences.username = username

                val bitmap = imageView_user_picture.drawable.toBitmap()
                sharedPreferences.profilePicture = saveImageToInternalStorage(this, bitmap)
                MoodChordsToast.showSuccessToast(
                    this,
                    resources.getString(R.string.login_welcome) + username,
                    Toast.LENGTH_LONG
                )
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }

    companion object {
        const val TAG = "HuaweiAuthCode"
    }

}