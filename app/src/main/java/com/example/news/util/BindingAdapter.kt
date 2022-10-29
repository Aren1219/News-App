package com.example.news.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.util.Util.formatDate

@BindingAdapter("loadFromUrl")
fun ImageView.loadFromUrl(url: String) {
    Glide
        .with(context)
        .load(url)
        .centerCrop()
        .placeholder(R.drawable.placeholder_image)
        .into(this)
}

@BindingAdapter("dateFormat")
fun TextView.dateFormat(date: String) {
    this.text = date.formatDate()
}