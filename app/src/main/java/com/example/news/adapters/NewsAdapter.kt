package com.example.news.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.data.model.Data
import com.example.news.R
import com.example.news.databinding.CardViewHomeBinding
import com.example.news.ui.MainViewModel
import com.example.news.util.ItemType
import com.example.news.util.Util.formatDate

class NewsAdapter(
    private var newsList: List<Data>,
    private val context: Context,
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
        Glide
            .with(context)
            .load(newsList[position].imageUrl)
            .centerCrop()
            .placeholder(R.drawable.placeholder_image)
            .into(holder.binding.image)
        holder.binding.apply {
            tvTitle.text = newsList[position].title
            tvDescription.text = newsList[position].description
            tvTime.text = newsList[position].publishedAt.formatDate()
            tvSource.text = newsList[position].source
            btnSave.text = itemType.btnText
            btnSave.setOnClickListener {
                if (btnSave.text == ItemType.NewsItem.btnText)
                    viewModel.saveNews(newsList[position])
                else
                    viewModel.deleteNews(newsList[position])
            }
            itemCardView.setOnClickListener {
                viewModel.webViewURL = newsList[position].url
                if (itemType is ItemType.NewsItem)
                    it.findNavController().navigate(R.id.action_newsFragment_to_webViewFragment)
                else
                    it.findNavController().navigate(R.id.action_savedNewsFragment_to_webViewFragment)
            }
        }
    }

    override fun getItemCount(): Int {
        return newsList.count()
    }

    fun updateList(list: List<Data>) {
        val index = itemCount
        newsList = list
//        newsList.addAll(list)
        notifyItemInserted(index)
    }
}