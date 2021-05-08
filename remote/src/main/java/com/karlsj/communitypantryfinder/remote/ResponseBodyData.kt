package com.karlsj.communitypantryfinder.remote

import kotlinx.serialization.Serializable

@Serializable
internal data class ResponseBodyData(
    val features: List<Feature>,
    val type: String
) {
    @Serializable
    data class Feature(
        val geometry: Geometry,
        val properties: Properties,
        val type: String
    ) {
        @Serializable
        data class Geometry(
            val coordinates: List<Double>,
            val type: String
        )

        @Serializable
        data class Properties(
            val barangay: String,
            val contact: String,
            val more: String,
            val municipality_city: String,
            val name: String,
            val number: String,
            val province: String,
            val region: String,
            val sched: String,
            val street_address: String,
            val supplies: String
        )
    }
}