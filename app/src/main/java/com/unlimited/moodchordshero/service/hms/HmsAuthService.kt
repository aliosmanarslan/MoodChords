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

package com.unlimited.moodchordshero.service.hms

import android.app.Activity
import android.util.Log
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.hms.support.hwid.result.AuthHuaweiId
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService

class HmsAuthService {

    companion object {

        fun checkAuthWithSilentSignIn(activity: Activity, callback: (Boolean) -> Unit) {
            val authParams: HuaweiIdAuthParams =
                HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM).createParams()
            val service: HuaweiIdAuthService = HuaweiIdAuthManager.getService(activity, authParams)
            val task: Task<AuthHuaweiId> = service.silentSignIn()

            task.addOnSuccessListener { authHuaweiId ->
                // Obtain the user's HUAWEI ID information.
                Log.i("silent-signing", "displayName:" + authHuaweiId.displayName)
                callback(true)
            }.addOnFailureListener { e ->
                // The sign-in failed. Try to sign in explicitly using getSignInIntent().
                if (e is ApiException) {
                    Log.i("silent-signing", "sign failed status:" + e.statusCode)
                }
                callback(false)
            }
        }

            fun signOut(activity: Activity, callback: (Boolean) -> Unit) {
                val authParams: HuaweiIdAuthParams =
                    HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM).createParams()
                val service: HuaweiIdAuthService = HuaweiIdAuthManager.getService(activity, authParams)
                val task: Task<Void>? = service.cancelAuthorization()

                if (task != null) {
                    task.addOnSuccessListener { authHuaweiId ->
                        // Obtain the user's HUAWEI ID information.
                        Log.i("Sing-Out result : ", "onSuccess: ")
                        callback(true)
                    }.addOnFailureListener { e ->
                        // The sign-in failed. Try to sign in explicitly using getSignInIntent().
                        if (e is ApiException) {
                            Log.i("sign-out failed", "sign-out failed status:" + e.statusCode)
                        }
                        callback(false)
                    }
                }
            }
    }

}