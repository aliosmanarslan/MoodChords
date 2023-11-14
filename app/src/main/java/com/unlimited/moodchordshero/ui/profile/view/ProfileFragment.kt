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

package com.unlimited.moodchordshero.ui.profile.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.unlimited.moodchordshero.R
import com.unlimited.moodchordshero.base.BaseFragment
import com.unlimited.moodchordshero.databinding.ProfileFragmentBinding
import com.unlimited.moodchordshero.service.hms.HmsAuthService
import com.unlimited.moodchordshero.ui.login.view.LoginActivity
import com.unlimited.moodchordshero.ui.profile.viewmodel.ProfileViewModel
import com.unlimited.moodchordshero.util.*
import com.unlimited.moodchordshero.util.SharedPreferencesHelper.clearValues
import com.unlimited.moodchordshero.util.SharedPreferencesHelper.language
import com.unlimited.moodchordshero.util.SharedPreferencesHelper.profilePicture
import com.unlimited.moodchordshero.util.SharedPreferencesHelper.username
import com.unlimited.moodchordshero.util.extensions.showAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.profile_fragment.*
import java.util.*

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ViewBinding, ProfileViewModel>() {

    private var imageUri: Uri? = null
    override val viewModel: ProfileViewModel? by viewModels()

    override fun getViewBinding(): ProfileFragmentBinding {
        return ProfileFragmentBinding.inflate(layoutInflater)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val sharedPreferences =
            SharedPreferencesHelper.customPreference(this.requireContext(), SHARED_PREFERENCES)

        observeLiveData()
        initUI(sharedPreferences)
    }

    private fun observeLiveData() {
        viewModel?.generalMoodMutableLiveData?.observe(
            requireActivity(), {
                generalModeResult.text = it
            })
    }

    private fun initUI(sharedPreferences: SharedPreferences) {
        viewModel?.getGeneralMood()

        if (sharedPreferences.language.equals(TURKISH_LANG)) {
            languageSwitch.toggle()
            languageSwitch.trackDrawable.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.login_bg_color),
                PorterDuff.Mode.SRC_IN
            )
        } else {
            languageSwitch.trackDrawable.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.grey),
                PorterDuff.Mode.SRC_IN
            )
        }

        val transformation = MultiTransformation(CenterCrop(), RoundedCorners(45))
        Glide.with(this).load(sharedPreferences.profilePicture).transform(transformation)
            .into(profileImageview)

        usernameText.text = sharedPreferences.username

        imageButton.setOnClickListener {
            findNavController().navigateUp()
        }

        usernameText.setOnClickListener {
            showChangeUsernameAlertDialog()
        }

        profileImageview.setOnClickListener {
            showChangeProfilePictureAlertDialog()
        }

        signOut.setOnClickListener {
            signOut()
        }

        languageSwitch.setOnCheckedChangeListener { _, isChecked ->
            checkLanguageButton(sharedPreferences, isChecked)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
            imageUri = data?.data
            val transformation = MultiTransformation(CenterCrop(), RoundedCorners(45))
            val sharedPreferences =
                SharedPreferencesHelper.customPreference(this.requireContext(), SHARED_PREFERENCES)
            sharedPreferences.profilePicture = imageUri.toString()
            Glide.with(this).load(sharedPreferences.profilePicture).transform(transformation)
                .into(profileImageview)
        }
    }

    private fun showChangeUsernameAlertDialog() {
        context?.showAlertDialog {
            it.message = getString(R.string.changeUserName)
            it.positiveButtonText = getString(R.string.ok)
            it.negativeButtonText = getString(R.string.cancel)
            it.isNegativeButtonEnable = true
            it.isInputFieldEnabled = true
            it.inputHint = usernameText.text.toString()
            it.onPositiveInputClick = {
                val sharedPreferences =
                    SharedPreferencesHelper.customPreference(
                        this.requireContext(),
                        SHARED_PREFERENCES
                    )
                sharedPreferences.username = it
                usernameText.text = it
            }
        }
    }

    private fun showChangeProfilePictureAlertDialog() {
        context?.showAlertDialog {
            it.message = getString(R.string.changeProfilPic)
            it.positiveButtonText = getString(R.string.ok)
            it.negativeButtonText = getString(R.string.cancel)
            it.isNegativeButtonEnable = true
            it.onPositiveClick = {
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, 100)
            }
        }
    }

    private fun signOut() {
        context?.showAlertDialog {
            it.message = getString(R.string.signOutMessage)
            it.positiveButtonText = getString(R.string.ok)
            it.negativeButtonText = getString(R.string.cancel)
            it.isNegativeButtonEnable = true
            it.onPositiveClick = {
                HmsAuthService.signOut(requireActivity()) { isSignOut ->
                    if (isSignOut) {
                        try {
                            SharedPreferencesHelper.customPreference(
                                requireContext(),
                                SHARED_PREFERENCES
                            ).clearValues()
                            val k = Intent(requireContext(), LoginActivity::class.java)
                            startActivity(k)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    private fun checkLanguageButton(sharedPreferences: SharedPreferences, isChecked: Boolean) {
        if (isChecked) {
            sharedPreferences.language = TURKISH_LANG
            val locale = Locale(TURKISH_LANG)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            val resources: Resources = resources
            resources.updateConfiguration(config, resources.displayMetrics)
            recreate(requireActivity())

        } else {
            sharedPreferences.language = ENG_LANG
            Locale.setDefault(Locale.ENGLISH)
            val config = Configuration()
            config.locale = Locale.ENGLISH
            val resources: Resources = resources
            resources.updateConfiguration(config, resources.displayMetrics)
            recreate(requireActivity())
        }
    }

}