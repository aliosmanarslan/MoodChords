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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.unlimited.moodchordshero.data.models.song.Song

class SearchResultAdapter(
    context: Context,
    @LayoutRes private val layoutResource: Int,
    private val allSongs: List<Song>,
    private val itemClicked: (item: Song) -> Unit
) :
    ArrayAdapter<Song>(context, layoutResource, allSongs),
    Filterable {

    private var mAllSongs: List<Song> = allSongs

    override fun getCount(): Int {
        return mAllSongs.size
    }

    override fun getItem(p0: Int): Song? {
        return mAllSongs.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        // Or just return p0
        return mAllSongs.get(p0).songObjectId.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(
            layoutResource,
            parent,
            false
        ) as TextView
        view.text = "${mAllSongs[position].songTitle} - ${mAllSongs[position].singer}"
        view.setOnClickListener {
            itemClicked(mAllSongs[position])
        }
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: Filter.FilterResults
            ) {
                mAllSongs = filterResults.values as List<Song>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString == null || queryString.isEmpty())
                    allSongs
                else
                    allSongs.filter {
                        it.songTitle.toLowerCase().contains(queryString) ||
                                it.singer?.contains(queryString, true)!!

                    }
                return filterResults
            }
        }

    }
}