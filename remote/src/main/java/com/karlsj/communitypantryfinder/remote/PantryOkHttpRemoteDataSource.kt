package com.karlsj.communitypantryfinder.remote

import com.karlsj.communitypantryfinder.data.Pantry
import com.karlsj.communitypantryfinder.data.PantryRemoteDataSource
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import javax.inject.Inject

internal class PantryOkHttpRemoteDataSource @Inject constructor(
    private val client: OkHttpClient,
    private val request: Request
) : PantryRemoteDataSource {

    override fun getPantries(): List<Pantry> {
        client.newCall(request)
            .execute()
            .use { response ->

                response.body.let { body ->
                    if (body == null) {
                        Timber.d("No data retrieved")
                        return emptyList()
                    }

                    Json.decodeFromString<ResponseBodyData>(body.string())
                }.let { data ->
                    if (data.features.isEmpty()) {
                        Timber.d("No data retrieved")
                        return emptyList()
                    }

                    return data.features.map { feature ->
                        val geometry = feature.geometry
                        val properties = feature.properties

                        Pantry(
                            latitude = geometry.coordinates[1],
                            longitude = geometry.coordinates[0],
                            name = properties.name.removeSurrounding("\"").trim(),
                            supplies = properties.supplies.removeSurrounding("\"").trim(),
                            contact = properties.contact.removeSurrounding("\"").trim(),
                            number = properties.number.removeSurrounding("\"").trim(),
                            streetAddress = properties.street_address.removeSurrounding("\"")
                                .trim(),
                            barangay = properties.barangay.removeSurrounding("\"").trim(),
                            city = properties.municipality_city.removeSurrounding("\"").trim(),
                            province = properties.province.removeSurrounding("\"").trim(),
                            region = properties.region.removeSurrounding("\"").trim(),
                            sched = properties.sched.removeSurrounding("\"").trim(),
                            more = properties.more.removeSurrounding("\"").trim()
                        )
                    }
                }
            }
    }

}