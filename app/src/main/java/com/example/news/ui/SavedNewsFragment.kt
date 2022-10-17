package com.example.news.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.adapters.NewsAdapter
import com.example.news.databinding.FragmentSavedNewsBinding
import com.example.news.util.ItemType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedNewsFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()
    lateinit var binding: FragmentSavedNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSavedNewsBinding.inflate(inflater)
        binding.recyclerviewFav.layoutManager = LinearLayoutManager(context)

        viewModel.savedList.observe(viewLifecycleOwner){
            if (it.isEmpty()) binding.tvEmptyList.visibility = View.VISIBLE
            else binding.tvEmptyList.visibility = View.INVISIBLE

            binding.recyclerviewFav.adapter = NewsAdapter(
                it, requireContext(), viewModel, ItemType.SavedNewsItem
            )
        }

        return binding.root
    }

}