package com.br.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("next")
    var nextPage: String?,
    @SerializedName("previous")
    var previousPage: String?,
    @SerializedName("results")
    val movieList: List<T>
)
