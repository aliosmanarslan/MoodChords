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

package com.unlimited.moodchordshero.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.unlimited.moodchordshero.R
import com.unlimited.moodchordshero.data.models.song.Song

class SongCardAdapter(
    private val context: Context,
    private val itemClicked: (item: Song) -> Unit
) :
    RecyclerView.Adapter<SongCardAdapter.ViewHolder>() {

    private var favList: List<Song>? = arrayListOf()

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgView: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            imgView = view.findViewById(R.id.fav_song_card_image_view)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fav_song_card_view, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val transformation = MultiTransformation(CenterCrop(), RoundedCorners(45))
        Glide.with(context).load(favList?.get(position)?.coverUrl).transform(transformation)
            .into(viewHolder.imgView)
        viewHolder.imgView.setOnClickListener {
            favList?.get(position)?.let { it1 ->
                itemClicked(
                    it1
                )
            }
        }
    }

    fun setFavList(songList: List<Song>?) {
        this.favList = songList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        if (favList?.size == null) return 0
        return favList?.size!!
    }
}