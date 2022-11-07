package com.example.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.domain.model.Data

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(data: Data)

    @Query("SELECT * FROM news")
    fun getAllNews(): LiveData<List<Data>>

    @Delete
    suspend fun deleteNews(data: Data)
}