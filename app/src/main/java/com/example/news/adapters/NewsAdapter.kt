package com.example.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.data.model.Data
import com.example.news.R
import com.example.news.databinding.CardViewHomeBinding
import com.example.news.ui.MainViewModel
import com.example.news.util.ItemType

class NewsAdapter(
    private var newsList: List<Data>,
    private val viewModel: MainViewModel,
    private val itemType: ItemType
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding: CardViewHomeBinding = CardViewHomeBinding.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            article = newsList[position]
            itemCardView.setOnClickListener {
                viewModel.webViewURL = newsList[position].url
                if (itemType is ItemType.NewsItem)
                    it.findNavController().navigate(R.id.action_newsFragment_to_webViewFragment)
                else
                    it.findNavController()
                        .navigate(R.id.action_savedNewsFragment_to_webViewFragment)
            }
            btnSave.text = itemType.btnText
            btnSave.setOnClickListener {
                if (btnSave.text == ItemType.NewsItem.btnText)
                    viewModel.saveNews(newsList[position])
                else
                    viewModel.deleteNews(newsList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return newsList.count()
    }

    fun updateList(list: List<Data>, notifyAll: Boolean = false) {
        newsList = list
        if (!notifyAll) {
            val index = itemCount
            notifyItemInserted(index)
        } else {
            notifyDataSetChanged()
        }
    }
}