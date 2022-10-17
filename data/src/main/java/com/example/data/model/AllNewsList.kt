package com.example.data.model


import com.google.gson.annotations.SerializedName

data class AllNewsList(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("meta")
    val meta: Meta
)