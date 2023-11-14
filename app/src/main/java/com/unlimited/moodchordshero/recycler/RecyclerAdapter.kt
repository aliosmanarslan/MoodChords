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

package com.unlimited.moodchordshero.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.unlimited.moodchordshero.R

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    val pics = intArrayOf(R.drawable.onboarding_one,R.drawable.onboarding_two,R.drawable.onboarding_three)
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    val image : ImageView
        init {
           image = itemView.findViewById(R.id.cardImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_view_home_screen,parent,false)
           return ViewHolder(v)
            }

    override fun getItemCount(): Int {
        return pics.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.setImageResource(pics[position])
    }
}