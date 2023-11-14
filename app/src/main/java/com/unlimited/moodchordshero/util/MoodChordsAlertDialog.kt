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

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.unlimited.moodchordshero.R
import kotlinx.android.synthetic.main.alert_dialog.*

class MoodChordsAlertDialog(
    context: Context,
    private val dialogProperties: MoodChordsAlertDialogProperties
) : Dialog(context) {

    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alert_dialog)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        positiveBtnAlertDialog.setOnClickListener { dismiss() }
        negativeBtnAlertDialog.setOnClickListener { dismiss() }
        customBtnAlertDialog.setOnClickListener { dismiss() }

        if (!dialogProperties.isNegativeButtonEnable)
            negativeBtnAlertDialog.visibility = View.GONE

        if (!dialogProperties.isInputFieldEnabled)
            editTextAlertDialog.visibility = View.GONE

        with(dialogProperties) {
            if (!positiveButtonText.isNullOrEmpty()) {
                with(positiveBtnAlertDialog) {
                    text = positiveButtonText
                    visibility = View.VISIBLE
                    if (isInputFieldEnabled) {
                        messageAlertDialog.requestLayout()
                        editTextAlertDialog.hint = inputHint

                        onPositiveInputClick?.also { positiveButtonClickListener ->
                            setOnClickListener {
                                positiveButtonClickListener(editTextAlertDialog.text.toString())
                                dismiss()
                            }
                        }

                    } else {
                        onPositiveClick?.also { positiveButtonClickListener ->
                            setOnClickListener {
                                positiveButtonClickListener()
                                dismiss()
                            }
                        }

                    }

                }
            }

            if (!negativeButtonText.isNullOrEmpty()) {
                with(negativeBtnAlertDialog) {
                    text = negativeButtonText
                    visibility = View.VISIBLE
                    onNegativeClick?.also { negativeButtonClickListener ->
                        setOnClickListener {
                            negativeButtonClickListener()
                            dismiss()
                        }
                    }
                }
            }

            if (!customButtonText.isNullOrEmpty()) {
                with(customBtnAlertDialog) {
                    text = customButtonText
                    visibility = View.VISIBLE
                    messageAlertDialog.visibility = View.GONE
                    editTextAlertDialog.visibility == View.GONE
                    updateLinearElementMargin(
                        view = positiveBtnAlertDialog,
                        leftMargin = null,
                        bottomMargin = null,
                        rightMargin = null,
                        topMargin = 0
                    )
                    onCustomClick?.also { customButtonClickListener ->
                        setOnClickListener {
                            customButtonClickListener()
                            dismiss()
                        }
                    }
                }
            }

            messageAlertDialog.text = message
            onDismiss?.also { onDismissListener ->
                setOnDismissListener { onDismissListener() }
            }

            setCancelable(cancellable)
        }

    }

}