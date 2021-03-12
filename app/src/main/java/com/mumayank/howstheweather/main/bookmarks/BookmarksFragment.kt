package com.mumayank.howstheweather.main.bookmarks

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mumayank.howstheweather.databinding.FragmentBookmarksBinding
import com.mumayank.howstheweather.main.details.DetailsActivity
import com.mumayank.howstheweather.repository.db.Bookmark

class BookmarksFragment : Fragment() {

    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BookmarksViewModel by viewModels()
    private val bookmarks = arrayListOf<Bookmark>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.recyclerView.adapter = RvAdapter(bookmarks, onClick = {
            startDetailsActivity(it)
        }, onRemove = {
            viewModel.removeBookmark(it)
        })
        viewModel.bookmarks.observe(viewLifecycleOwner) {
            bookmarks.clear()
            bookmarks.addAll(it)
            binding.recyclerView.adapter!!.notifyDataSetChanged()

            if (bookmarks.size == 0) {
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.emptyView.visibility = View.GONE
            }
        }
    }

    private fun startDetailsActivity(bookmark: Bookmark) {
        startActivity(
            Intent(
                activity,
                DetailsActivity::class.java
            ).putExtra(DetailsActivity.IE_CITY_LAT, bookmark.cityLat)
                .putExtra(DetailsActivity.IE_CITY_LON, bookmark.cityLon)
                .putExtra(DetailsActivity.IE_CITY_NAME, bookmark.cityName)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}