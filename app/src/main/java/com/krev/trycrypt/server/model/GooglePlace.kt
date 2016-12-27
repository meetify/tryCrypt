package com.krev.trycrypt.server.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GooglePlace(
        var results: List<Result> = ArrayList(),
        var status: String = "",
        var htmlAttributions: List<String> = ArrayList(),
        var nextPageToken: String = "") : Serializable {

    data class AddressComponent(
            var longName: String = "",
            var shortName: String = "",
            var types: List<String>? = ArrayList()) : Serializable

    data class AlternativeId(
            var placeId: String = "",
            var scope: String = "") : Serializable

    data class Aspect(
            var rating: Int = 0,
            var type: String = "") : Serializable

    data class DayTime(
            var day: Int = 0,
            var time: Int = 0) : Serializable

    data class Geometry(
            var location: GoogleLocation = GoogleLocation(),
            var viewport: Viewport = Viewport()) : Serializable

    data class OpeningHours(
            var openNow: Boolean = false,
            var weekdayText: List<String> = ArrayList(),
            var periods: List<Period> = ArrayList()) : Serializable

    data class GoogleLocation(
            var lat: Double = 0.0,
            var lng: Double = 0.0) : Serializable

    data class Period(
            var open: DayTime = DayTime(),
            var close: DayTime = DayTime()) : Serializable

    data class Photo(
            var width: Double? = 0.0,
            var height: Double = 0.0,
            var htmlAttributions: List<String> = ArrayList(),
            var photoReference: String = "") : Serializable

    data class Result(
            var geometry: Geometry = Geometry(),
            var openingHours: OpeningHours = OpeningHours(),
            var formattedAddress: String = "",
            var formattedPhoneNumber: String = "",
            var internationalPhoneNumber: String = "",
            var placeId: String = "",
            var icon: String = "",
            var id: String = "",
            var name: String = "",
            var reference: String = "",
            var scope: String = "",
            var vicinity: String = "",
            var website: String = "",
            var url: String = "",
            var rating: Double = 0.0,
            var permanentlyClosed: Boolean = false,
            var utcOffset: Int = 0,
            var priceLevel: Int = 0,
            var types: List<String> = ArrayList(),
            var photos: List<Photo> = ArrayList(),
            var alternativeIds: List<AlternativeId> = ArrayList(),
            var addressComponents: List<AddressComponent> = ArrayList(),
            var reviews: List<Review> = ArrayList()) : Serializable

    data class Review(
            var aspects: List<Aspect> = ArrayList(),
            var authorName: String = "",
            var authorUrl: String = "",
            var language: String = "",
            var rating: Double = 0.0,
            var text: String = "",
            var time: Int = 0) : Serializable

    data class Viewport(
            var northeast: GoogleLocation = GoogleLocation(),
            var southwest: GoogleLocation = GoogleLocation()) : Serializable
}
