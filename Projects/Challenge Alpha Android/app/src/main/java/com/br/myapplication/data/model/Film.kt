package com.br.myapplication.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "films_table")
data class Film(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var image: String = "",
    val characters: List<String>,
    val created: String,
    val director: String,
    val edited: String,
    @SerializedName("episode_id")
    val episodeId: Int,
    @SerializedName("opening_crawl")
    val openingCrawl: String,
    val planets: List<String>,
    val producer: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val species: List<String>,
    val starships: List<String>,
    val title: String,
    val url: String,
    val vehicles: List<String>,
    var isFavorite: Boolean = false
)

