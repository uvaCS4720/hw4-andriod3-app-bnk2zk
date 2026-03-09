package edu.nd.pmcburne.hello.data

import android.text.Html
import edu.nd.pmcburne.hello.network.RetrofitClient
import kotlinx.coroutines.flow.Flow

class LocationRepository(private val locationDao: LocationDao) {

    val allLocations: Flow<List<LocationEntity>> = locationDao.getAllLocations()

    suspend fun refreshLocations() {
        try {
            val response = RetrofitClient.service.getPlacemarks()
            val entities = response.map { res ->
                LocationEntity(
                    id = res.id,
                    name = res.name,
                    // Clean HTML entities like &apos;
                    description = Html.fromHtml(res.description, Html.FROM_HTML_MODE_LEGACY).toString(),
                    latitude = res.visualCenter.latitude,
                    longitude = res.visualCenter.longitude,
                    tags = res.tagList
                )
            }
            locationDao.insertAll(entities)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
