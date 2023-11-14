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

package com.unlimited.moodchordshero.ui.onboarding.viewpager

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.unlimited.moodchordshero.R
import com.unlimited.moodchordshero.ui.login.view.LoginActivity
import com.unlimited.moodchordshero.util.ONBOARDING_STRING_OBJECT
import com.unlimited.moodchordshero.util.SHARED_PREFERENCES
import com.unlimited.moodchordshero.util.SharedPreferencesHelper
import com.unlimited.moodchordshero.util.SharedPreferencesHelper.isFirstTime
import kotlinx.android.synthetic.main.view_pager_content.*

class ViewPagerContentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.view_pager_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.takeIf { it.containsKey(ONBOARDING_STRING_OBJECT) }?.apply {
            textView_onboarding_header.text = getStringArray(ONBOARDING_STRING_OBJECT)!![0]
            textView_onboarding_subtext.text = getStringArray(ONBOARDING_STRING_OBJECT)!![1]
            changeImage(getStringArray(ONBOARDING_STRING_OBJECT)!![2])
        }

        val prefs = SharedPreferencesHelper.customPreference(
            requireContext(),
            SHARED_PREFERENCES
        )

        skip_text.setOnClickListener {
            //Handle Skip Click event
            prefs.isFirstTime = false
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()

        }
        button_onboarding_done.setOnClickListener {
            prefs.isFirstTime = false
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }
    }

    private fun changeImage(screen: String) {
        when (screen) {
            "1" ->
                imageView_onboarding.setImageResource(R.drawable.onboarding_one)
            "2" ->
                imageView_onboarding.setImageResource(R.drawable.onboarding_two)
            "3" ->
                imageView_onboarding.setImageResource(R.drawable.onboarding_three)
        }

        if (screen == "3") {
            button_onboarding_done.visibility = View.VISIBLE
        }
    }

}