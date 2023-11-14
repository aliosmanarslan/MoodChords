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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.unlimited.moodchordshero.R
import com.unlimited.moodchordshero.ui.onboarding.adapter.OnboardingViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_onboarding_view_pager.*


class OnboardingViewPagerFragment : Fragment() {

    private val mPageNumbers = 3

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = OnboardingViewPagerAdapter(this, getListOfPagerContents(), mPageNumbers)
        onboarding_pager.adapter = pagerAdapter

        TabLayoutMediator(onboarding_tab_layout, onboarding_pager) { tab, position -> }.attach()
    }

    private fun getListOfPagerContents(): List<Array<String>> {

        val ar1 =
            arrayOf(getString(R.string.selfie), getString(R.string.selfieDesc), "1")
        val ar2 =
            arrayOf(getString(R.string.chord), getString(R.string.analyzeDesc), "2")
        val ar3 =
            arrayOf(getString(R.string.analyze), getString(R.string.chordDesc), "3")
        return listOf(ar1, ar2, ar3)
    }
}