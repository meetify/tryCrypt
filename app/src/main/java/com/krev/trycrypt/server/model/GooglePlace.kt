package com.krev.trycrypt.server.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GooglePlace(
        var results: List<Result> = ArrayList(),
        var status: String = "",
        @JsonProperty("html_attributions") var htmlAttributions: List<String> = ArrayList(),
        @JsonProperty("next_page_token") var nextPageToken: String = "") : Serializable {

    data class AddressComponent(
            @JsonProperty("long_name") var longName: String = "",
            @JsonProperty("short_name") var shortName: String = "",
            var types: List<String>? = ArrayList()) : Serializable

    data class AlternativeId(
            @JsonProperty("place_id") var placeId: String = "",
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
            @JsonProperty("open_now") var openNow: Boolean = false,
            @JsonProperty("weekday_text") var weekdayText: List<String> = ArrayList(),
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
            @JsonProperty("html_attributions") var htmlAttributions: List<String> = ArrayList(),
            @JsonProperty("photo_reference") var photoReference: String = "") : Serializable

    data class Result(
            var geometry: Geometry = Geometry(),
            @JsonProperty("opening_hours") var openingHours: OpeningHours = OpeningHours(),
            @JsonProperty("formatted_address") var formattedAddress: String = "",
            @JsonProperty("formatted_phone_number") var formattedPhoneNumber: String = "",
            @JsonProperty("international_phone_number") var internationalPhoneNumber: String = "",
            @JsonProperty("place_id") var placeId: String = "",
            var icon: String = "",
            var id: String = "",
            var name: String = "",
            var reference: String = "",
            var scope: String = "",
            var vicinity: String = "",
            var website: String = "",
            var url: String = "",
            var rating: Double = 0.0,
            @JsonProperty("permanently_closed") var permanentlyClosed: Boolean = false,
            @JsonProperty("utc_offset") var utcOffset: Int = 0,
            @JsonProperty("price_level") var priceLevel: Int = 0,
            var types: List<String> = ArrayList(),
            var photos: List<Photo> = ArrayList(),
            @JsonProperty("alt_ids") var alternativeIds: List<AlternativeId> = ArrayList(),
            @JsonProperty("address_components") var addressComponents: List<AddressComponent> = ArrayList(),
            var reviews: List<Review> = ArrayList()) : Serializable

    data class Review(
            var aspects: List<Aspect> = ArrayList(),
            @JsonProperty("author_name") var authorName: String = "",
            @JsonProperty("author_url") var authorUrl: String = "",
            var language: String = "",
            var rating: Double = 0.0,
            var text: String = "",
            var time: Int = 0) : Serializable

    data class Viewport(
            var northeast: GoogleLocation = GoogleLocation(),
            var southwest: GoogleLocation = GoogleLocation()) : Serializable
}
