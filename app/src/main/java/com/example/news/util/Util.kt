package com.example.news.util


object Util {

    //"2022-09-03T14:36:55.000000Z" to "14:36 03-09-2022"
    fun String.formatDate() =
        "${this.substring(11, 16)} ${this.substring(8, 10)}-${this.substring(5, 7)}-${this.take(4)}"

}