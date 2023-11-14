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

import com.huawei.hms.mlsdk.face.MLFaceEmotion

class FaceDetectionResponse(emotionResult: MLFaceEmotion) {

    var angryProbability: Float? = null
    var disgustProbability: Float? = null
    var fearProbability: Float? = null
    var neutralProbability: Float? = null
    var sadProbability: Float? = null
    var smilingProbability: Float? = null
    var surpriseProbability: Float? = null

    init {
        setResults(emotionResult)
    }

    private fun setResults(emotionResult: MLFaceEmotion) {
        angryProbability = emotionResult.angryProbability
        disgustProbability = emotionResult.disgustProbability
        fearProbability = emotionResult.fearProbability
        neutralProbability = emotionResult.neutralProbability
        sadProbability = emotionResult.sadProbability
        smilingProbability = emotionResult.smilingProbability
        surpriseProbability = emotionResult.surpriseProbability

    }

    fun getEmotion(): EmotionState {
        val map = mapOf(
            Pair(EmotionState.ANGRY, angryProbability),
            Pair(EmotionState.DISGUST, disgustProbability),
            Pair(EmotionState.FEAR, fearProbability),
            Pair(EmotionState.NEUTRAL, neutralProbability),
            Pair(EmotionState.SAD, sadProbability),
            Pair(EmotionState.SMILING, smilingProbability),
            Pair(EmotionState.SURPRISE, surpriseProbability)
        )

        val sortedMap = map.toList().sortedByDescending { (key, value) -> value }.toMap()

        val entry = sortedMap.entries.iterator().next() // get first element

        return entry.key
    }

    override fun toString(): String {
        return "Emotions(angryProbability=$angryProbability, disgustProbability=$disgustProbability, fearProbability='$fearProbability', neutralProbability='$neutralProbability', sadProbability=$sadProbability, smilingProbability=$smilingProbability, surpriseProbability=$surpriseProbability)"
    }

}