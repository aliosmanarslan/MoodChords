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

package com.unlimited.moodchordshero.ui.chords.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.unlimited.moodchordshero.R
import com.unlimited.moodchordshero.base.BaseFragment
import com.unlimited.moodchordshero.data.models.song.Song
import com.unlimited.moodchordshero.databinding.ChordsFragmentBinding
import com.unlimited.moodchordshero.service.hms.FaceDetectionService
import com.unlimited.moodchordshero.ui.chords.viewmodel.ChordsViewModel
import com.unlimited.moodchordshero.util.CAMERA_REQUEST_CODE
import com.unlimited.moodchordshero.util.MoodChordsToast
import com.unlimited.moodchordshero.util.WEBVIEW_URL_PATH
import com.unlimited.moodchordshero.util.extensions.showAlertDialog
import com.unlimited.moodchordshero.util.updateElementMargin
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.chord_content_layout.*
import kotlinx.android.synthetic.main.chords_fragment.*

@AndroidEntryPoint
class ChordsFragment : BaseFragment<ViewBinding, ChordsViewModel>() {

    override val viewModel: ChordsViewModel? by viewModels()
    private var selectedImageBitmap: Bitmap? = null

    override fun getViewBinding(): ChordsFragmentBinding {
        return ChordsFragmentBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            viewModel!!.emotionArgs.value = ChordsFragmentArgs.fromBundle(it).emotion
            viewModel!!.songArgs.value = ChordsFragmentArgs.fromBundle(it).songObject
        }

        initUI()
        if (viewModel!!.emotionArgs.value != null) {
            observeLiveDataWithEmotionArgs()
        } else if (viewModel!!.songArgs.value != null) {
            observeLiveDataWithSongArgs()
        }
    }

    private fun initUI() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
        }
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.toolbar_back)

        webView_chord_content.settings.useWideViewPort = true
        webView_chord_content.settings.loadWithOverviewMode = true

        app_bar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                Log.d("STATE", state!!.name)
                if (state.name == State.COLLAPSED.name) {
                    imageButton_back_home.visibility = View.VISIBLE
                    updateElementMargin(
                        view = textView_chord_song_title,
                        leftMargin = 32,
                        bottomMargin = null,
                        rightMargin = null,
                        topMargin = null
                    )
                } else {
                    imageButton_back_home.visibility = View.GONE
                    updateElementMargin(
                        view = textView_chord_song_title,
                        leftMargin = 0,
                        bottomMargin = null,
                        rightMargin = null,
                        topMargin = null
                    )
                }
            }
        })

        button_chord_collapse_menu.setOnClickListener {
            val song = viewModel?.song?.value
            context?.showAlertDialog {
                it.positiveButtonText = getString(R.string.chordsTryAgain)

                if (song?.isFavorite == false) {
                    it.negativeButtonText = getString(R.string.chordsAddFav)
                } else {
                    it.negativeButtonText = getString(R.string.chordsRemoveFav)
                }

                it.customButtonText = getString(R.string.chordsTakeSelfie)

                it.isNegativeButtonEnable = true

                it.onPositiveClick = {
                    viewModel!!.emotionArgs.observe(viewLifecycleOwner, Observer { emotion ->
                        viewModel?.getSongByEmotion(emotion)
                    })

                    viewModel?.song?.observe(viewLifecycleOwner, Observer { song ->
                        setSong(song)
                    })
                }

                it.onCustomClick = {
                    openCamera()
                }

                it.onNegativeClick = {
                    if (song?.isFavorite == false) {
                        song.isFavorite = true
                        viewModel!!.addToFavList(song)
                        MoodChordsToast.showSuccessToast(
                            requireContext(),
                            getString(R.string.chordsAddFavSuccess),
                            Toast.LENGTH_SHORT
                        )
                    } else {
                        song?.isFavorite = false
                        viewModel!!.addToFavList(song!!)
                        MoodChordsToast.showSuccessToast(
                            requireContext(),
                            getString(R.string.chordsRemoveFavSuccess),
                            Toast.LENGTH_SHORT
                        )
                    }
                }
            }
        }

        imageButton_back_home.setOnClickListener {
            findNavController().navigateUp()
        }

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun observeLiveDataWithEmotionArgs() {
        viewModel!!.emotionArgs.observe(viewLifecycleOwner, Observer {
            viewModel?.getSongByEmotion(it)
        })

        viewModel?.song?.observe(viewLifecycleOwner, Observer { song ->
            setSong(song)
        })
    }

    private fun observeLiveDataWithSongArgs() {
        viewModel?.songArgs?.observe(viewLifecycleOwner, Observer { song ->
            setSong(song)
            viewModel?.song?.value = song
            viewModel?.emotionArgs?.value = song.emotionCategory
        })
    }

    private fun openCamera() {
        if (context?.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        } else {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                MoodChordsToast.showErrorToast(
                    requireContext(),
                    resources.getString(R.string.cameraPermissionDenied),
                    Toast.LENGTH_LONG
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            selectedImageBitmap = data?.extras?.get("data") as Bitmap
            getFaceEmotion(selectedImageBitmap!!)
        }
    }

    private fun getFaceEmotion(bitmap: Bitmap, callback: (() -> Unit)? = null) {
        FaceDetectionService.getFaceDetection(bitmap) {
            viewModel!!.newEmotionArgs.value = it?.value

            callback?.invoke()

            viewModel!!.newEmotionArgs.observe(viewLifecycleOwner, Observer { emotion ->
                viewModel?.insertMood(emotion)
                viewModel?.getSongByEmotion(emotion)
                viewModel?.emotionArgs?.value = emotion
            })

            viewModel?.song?.observe(viewLifecycleOwner, Observer { song ->
                setSong(song)
            })
        }
    }

    private fun setSong(song: Song) {
        textView_chord_song_title.text = song.songTitle
        textView_chord_song_artist.text = song.singer
        textView_emotion_category.text = getString(R.string.chord_emotion) + song.emotionCategory
        webView_chord_content.loadUrl(WEBVIEW_URL_PATH + song.chordsLink)
        Glide.with(this).load(song.coverUrl).into(imageView_chord_pic)
    }

    companion object {
        fun newInstance() = ChordsFragment()
    }

}