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

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.face.MLFaceAnalyzerSetting

class FaceDetectionService {

    companion object {

        private const val TAG = "FaceDetectionService"

        @SuppressLint("LongLogTag")
        fun getFaceDetection(
            bitmap: Bitmap,
            callback: (EmotionState?) -> Unit
        ) {

            val setting = MLFaceAnalyzerSetting.Factory() // Set whether to detect key face points.
                .setKeyPointType(MLFaceAnalyzerSetting.TYPE_KEYPOINTS) // Set whether to detect facial features and expressions.
                .setFeatureType(MLFaceAnalyzerSetting.TYPE_FEATURES) // Enable only facial expression detection and gender detection.
                .setFeatureType(MLFaceAnalyzerSetting.TYPE_FEATURE_EMOTION or MLFaceAnalyzerSetting.TYPE_FEATURE_GENDAR) // Set whether to detect face contour points.
                .setShapeType(MLFaceAnalyzerSetting.TYPE_SHAPES) // Set whether to enable face tracking and specify the fast tracking mode.
                .setPerformanceType(MLFaceAnalyzerSetting.TYPE_SPEED) // Set whether to enable pose detection (enabled by default).
                .setPoseDisabled(true)
                .create()

            val analyzer = MLAnalyzerFactory.getInstance().getFaceAnalyzer(setting)

            val frame = MLFrame.fromBitmap(bitmap)

            val task = analyzer.asyncAnalyseFrame(frame)

            task.addOnSuccessListener { results ->

                results.forEach {
                    val emotions = it.emotions
                    val faceEmotionResult = FaceDetectionResponse(emotions).getEmotion()
                    callback(faceEmotionResult)
                }
                // Detection success.
            }.addOnFailureListener {
                // Detection failure.
                Log.e(TAG, it.message)
                callback(null)
            }
        }

    }

}
