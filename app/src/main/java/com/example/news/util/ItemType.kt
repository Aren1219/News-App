package com.example.news.util

sealed class ItemType(
    val btnText:  String
){
    object NewsItem: ItemType("save")
    object SavedNewsItem: ItemType("delete")
}
