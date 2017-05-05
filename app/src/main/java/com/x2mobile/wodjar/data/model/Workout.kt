package com.x2mobile.wodjar.data.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.x2mobile.wodjar.business.Constants
import com.x2mobile.wodjar.data.model.adapter.WorkoutCategoryAdapter

class Workout {

    @SerializedName("id")
    var id: Int = Constants.ID_NA

    @SerializedName("name")
    var name: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("history")
    var history: String? = null

    @SerializedName("category")
    @JsonAdapter(WorkoutCategoryAdapter::class)
    var category: WorkoutCategory = WorkoutCategory.CUSTOM

    @SerializedName("image")
    var image: String? = null

    @SerializedName("video")
    var video: String? = null

    @SerializedName("favorites")
    var favorite: Boolean = false

    @SerializedName("completed")
    var completed: Boolean = false

}