package com.example.news.ui.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.adapters.NewsAdapter
import com.example.news.databinding.FragmentHomeBinding
import com.example.news.ui.MainViewModel
import com.example.news.util.ItemType
import com.example.news.util.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewsFragment : Fragment() {

    private val log = "NewsFragment"

    private var adapter: NewsAdapter? = null
    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentHomeBinding

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val lastVisibleItemPos = layoutManager.findLastVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount
            if (lastVisibleItemPos == totalItemCount - 1) viewModel.getNewsList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        binding.recyclerviewHome.addOnScrollListener(this@NewsFragment.scrollListener)
        binding.recyclerviewHome.layoutManager = LinearLayoutManager(context)
        adapter = NewsAdapter(emptyList(), requireContext(), viewModel, ItemType.NewsItem)
        binding.recyclerviewHome.adapter = adapter

        Log.d(log, "onCreateView")

        viewModel.newsList.observe(viewLifecycleOwner) {
            Log.d(log, "running observer block")
            when (it) {
                is Resource.Loading -> {
                    binding.loadingProgressBarHome.show()
                    binding.tvError.visibility = View.INVISIBLE
                }
                is Resource.Success<*> -> {
                    binding.tvError.visibility = View.INVISIBLE
                    binding.loadingProgressBarHome.hide()
                    it.data?.let { list -> adapter!!.updateList(list) }
                }
                is Resource.Error -> {
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = it.message
                    binding.loadingProgressBarHome.hide()
                }
            }
        }

        return binding.root
    }

//    private fun updateList(list: List<Data>) {
//        if (adapter != null && adapter!!.itemCount != list.size) {
//            //updateLIst
//            adapter?.updateList(list)
//            binding.recyclerviewHome.adapter = adapter
//            Log.d(log, "updateList")
//        } else {
//            //create new adapter
//            adapter = NewsAdapter(list, requireContext(), viewModel, ItemType.NewsItem)
//            binding.recyclerviewHome.adapter = adapter
//            Log.d(log, "create adapter")
//        }
//    }
}