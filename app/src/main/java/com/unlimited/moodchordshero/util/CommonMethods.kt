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

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.unlimited.moodchordshero.util.SharedPreferencesHelper.language
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

fun setLocale(activity: Activity, s: String) {
    val sharedPref = SharedPreferencesHelper.customPreference(activity, SHARED_PREFERENCES)
    val deviceLocal = Locale.getDefault().language
    val newLocale = Locale(s)
    Log.i(
        "setLocale",
        "sharedPref.language: ${sharedPref.language} deviceLocal:$deviceLocal newLocale.language: ${newLocale.language}"
    )
    if (sharedPref.language != deviceLocal || deviceLocal != newLocale.language) {
        sharedPref.language = newLocale.language
        activity.recreate()
    }
}

fun pickImageFromGallery(activity: Activity) {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_GET_CONTENT
    activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
}

fun pickImageFromGallery(fragment: Fragment) {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_GET_CONTENT
    fragment.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
}

fun saveImageToInternalStorage(
    context: Context,
    bitmapImage: Bitmap,
    directoryName: String = "images",
    fileName: String = UUID.randomUUID().toString()
): String? {
    val cw = ContextWrapper(context)
    // path to /data/data/yourapp/app_data/imageDir
    val directory: File = cw.getDir(directoryName, Context.MODE_PRIVATE)

    // Create imageDir
    val file = File(directory, "$fileName.png")
    var fos: FileOutputStream? = null
    try {
        fos = FileOutputStream(file)
        // Use the compress method on the BitMap object to write image to the OutputStream
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    return file.absolutePath
}

fun updateElementMargin(view: View, topMargin: Int?, bottomMargin: Int?, leftMargin: Int?, rightMargin: Int?) {
    val newParams = view.layoutParams as ConstraintLayout.LayoutParams
    if (leftMargin != null) {
        newParams.leftMargin = leftMargin
    }
    if (rightMargin != null) {
        newParams.rightMargin = rightMargin
    }
    if (topMargin != null) {
        newParams.topMargin = topMargin
    }
    if (bottomMargin != null) {
        newParams.bottomMargin = bottomMargin
    }
    view.layoutParams = newParams
}

fun updateLinearElementMargin(view: View, topMargin: Int?, bottomMargin: Int?, leftMargin: Int?, rightMargin: Int?) {
    val newParams = view.layoutParams as LinearLayout.LayoutParams
    if (leftMargin != null) {
        newParams.leftMargin = leftMargin
    }
    if (rightMargin != null) {
        newParams.rightMargin = rightMargin
    }
    if (topMargin != null) {
        newParams.topMargin = topMargin
    }
    if (bottomMargin != null) {
        newParams.bottomMargin = bottomMargin
    }
    view.layoutParams = newParams
}