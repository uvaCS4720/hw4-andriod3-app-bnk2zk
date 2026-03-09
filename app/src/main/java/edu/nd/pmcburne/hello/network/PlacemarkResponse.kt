package edu.nd.pmcburne.hello.network

import com.squareup.moshi.Json

data class PlacemarkResponse(
    val id: Int,
    val name: String,
    val description: String,
    @Json(name = "tag_list") val tagList: List<String>,
    @Json(name = "visual_center") val visualCenter: VisualCenter
)

data class VisualCenter(
    val latitude: Double,
    val longitude: Double
)
