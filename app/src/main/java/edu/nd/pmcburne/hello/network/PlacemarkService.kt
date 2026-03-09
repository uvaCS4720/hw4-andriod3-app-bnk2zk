package edu.nd.pmcburne.hello.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface PlacemarkService {
    @GET("placemarks.json")
    suspend fun getPlacemarks(): List<PlacemarkResponse>
}

object RetrofitClient {
    private const val BASE_URL = "https://www.cs.virginia.edu/~wxt4gm/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val service: PlacemarkService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PlacemarkService::class.java)
    }
}
