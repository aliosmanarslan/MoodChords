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

package com.unlimited.moodchordshero.ui.home.view

import SearchResultAdapter
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.unlimited.moodchordshero.R
import com.unlimited.moodchordshero.base.BaseFragment
import com.unlimited.moodchordshero.data.models.song.Song
import com.unlimited.moodchordshero.databinding.HomeFragmentBinding
import com.unlimited.moodchordshero.service.hms.EmotionState
import com.unlimited.moodchordshero.service.hms.FaceDetectionService
import com.unlimited.moodchordshero.ui.home.adapter.SongCardAdapter
import com.unlimited.moodchordshero.ui.home.viewmodel.HomeViewModel
import com.unlimited.moodchordshero.util.CAMERA_REQUEST_CODE
import com.unlimited.moodchordshero.util.MoodChordsToast
import com.unlimited.moodchordshero.util.SHARED_PREFERENCES
import com.unlimited.moodchordshero.util.SharedPreferencesHelper
import com.unlimited.moodchordshero.util.SharedPreferencesHelper.profilePicture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_fragment.*

@AndroidEntryPoint
class HomeFragment : BaseFragment<ViewBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel? by viewModels()
    private lateinit var favSongsAdapter: SongCardAdapter
    private var selectedImageBitmap: Bitmap? = null
    lateinit var adapter: ArrayAdapter<*>
    private lateinit var allSongList: List<Song>

    override fun getViewBinding(): HomeFragmentBinding {
        return HomeFragmentBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = SharedPreferencesHelper.customPreference(
            requireContext(),
            SHARED_PREFERENCES
        )

        val transformation = MultiTransformation(CenterCrop(), RoundedCorners(45))

        Glide.with(this).load(sharedPreferences.profilePicture).transform(transformation)
            .into(imageView_user_picture)


        cameraButton.setOnClickListener {
            openCamera()
        }

        buttonProfile.setOnClickListener {
            val directions = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
            findNavController().navigate(directions)
        }

        observeLiveData()
        initUI()
    }

    override fun onResume() {
        super.onResume()
        observeLiveData()
        viewModel?.getWeeklyMood()
        viewModel?.getFavList()
    }

    private fun initUI() {
        viewModel?.getAllSongList()

        fav_song_recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        favSongsAdapter = SongCardAdapter(context = requireContext(),
            itemClicked = { item ->
                val directions =
                    HomeFragmentDirections.actionHomeFragmentToChordsFragment(item, null)
                findNavController().navigate(directions)
            })

        fav_song_recyclerView.adapter = favSongsAdapter
    }

    private fun observeLiveData() {
        viewModel?.weeklyMoodMutableLiveData?.observe(
            viewLifecycleOwner, Observer {
                moodType.text = it
            })

        viewModel?.favListMutableLiveData?.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                noDataView.visibility = View.VISIBLE
                fav_song_recyclerView.visibility = View.GONE
            } else {
                noDataView.visibility = View.GONE
                fav_song_recyclerView.visibility = View.VISIBLE
                favSongsAdapter.setFavList(it)
            }
        })

        viewModel?.allSongListMutableLiveData?.observe(
            viewLifecycleOwner, Observer {
                allSongList = (it)
                setSearchAdapter()
            })
    }

    private fun setSearchAdapter() {
        searchView.setOnClickListener { searchView.isIconified = false }
        searchView.setOnCloseListener {
            listView.visibility = View.GONE
            false
        }

        adapter = SearchResultAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            allSongList,
            itemClicked = { item ->
                searchView.setQuery("", false)
                searchView.isIconified = true
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToChordsFragment(
                        item,
                        null
                    )
                )
            }
        )
        listView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                listView.visibility = View.VISIBLE
                adapter.filter.filter(newText)
                return false
            }
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
        FaceDetectionService.getFaceDetection(bitmap) { emotion ->
            //  Log.d("ResultEmotion", it?.value + " " + it.toString())
            callback?.invoke()
            emotion?.let {
                viewModel?.insertMood(it.value)
                navigateToChords(it)
            }
        }
    }

    private fun navigateToChords(emotion: EmotionState) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToChordsFragment(
                null,
                emotion.value
            )
        )
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
