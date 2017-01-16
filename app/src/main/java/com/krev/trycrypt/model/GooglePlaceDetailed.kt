package com.krev.trycrypt.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

/**
 * Google place representation.
 * All documentation about this class & inner classes was taken from
 * [Google documentation](https://developers.google.com/places/web-service/details)
 * and it's licensed under the [CC BY 3.0](https://creativecommons.org/licenses/by/3.0/).
 * No changes were made, except of some in-page redirects.
 * @since    0.3.0
 * @author  Dmitry Baynak
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class GooglePlaceDetailed(
        var result: GooglePlaceDetailed.Result = Result(),
        val status: String = "",
        @JsonProperty("html_attributions") val htmlAttributions: List<String> = java.util.ArrayList(),
        @JsonProperty("next_page_token") val nextPageToken: String = "") : Serializable {

    /**
     * @property types   is an array indicating the type of the address component.
     * @property longName  is the full text description or name of the address component.
     * @property shortName is an abbreviated textual name for the address component, if available.
     */
    data class AddressComponent(
            @JsonProperty("long_name") val longName: String = "",
            @JsonProperty("short_name") val shortName: String = "",
            val types: List<String> = java.util.ArrayList()) : Serializable

    /**
     * @property placeId the most likely reason for a place to have an alternative place ID is if your application adds a place and receives an application-scoped place ID, then later receives a Google-scoped place ID after passing the moderation process.
     * @property scope  the scope of an alternative place ID will always be APP, indicating that the alternative place ID is recognised by your application only.
     */
    data class AlternativeId(
            @JsonProperty("place_id") val placeId: String = "",
            val scope: String = "") : Serializable

    /**
     * @property type  the name of the aspect that is being rated. The following types are supported: appeal, atmosphere, decor, facilities, food, overall, quality and service.
     * @property rating the user's rating for this particular aspect, from 0 to 3.
     */
    data class Aspect(
            val rating: Int = 0,
            val type: String = "") : Serializable

    /**
     * @property day  a number from 0–6, corresponding to the days of the week, starting on Sunday. For example, 2 means Tuesday.
     * @property time  may contain a time of day in 24-hour HHMM format. Values are in the range 0000–2359. The time will be reported in the place’s time zone.
     */
    data class DayTime(
            val day: Int = 0,
            val time: Int = 0) : Serializable

    /**
     * @property location the geocoded latitude,longitude value for this place.
     * @property viewport the preferred viewport when displaying this place on a map as a LatLngBounds if it is known.
     */
    data class Geometry(
            val location: GooglePlaceDetailed.GoogleLocation = GooglePlaceDetailed.GoogleLocation(),
            val viewport: GooglePlaceDetailed.Viewport = GooglePlaceDetailed.Viewport()) : Serializable

    /**
     * @property openNow is a boolean value indicating if the place is open at the current time.
     * @property periods is an array of opening periods covering seven days, starting from Sunday, in chronological order.
     * @property weekdayText is an array of seven strings representing the formatted opening hours for each day of the week.
     */
    data class OpeningHours(
            @JsonProperty("open_now") val openNow: Boolean = false,
            @JsonProperty("weekday_text") val weekdayText: List<String> = java.util.ArrayList(),
            val periods: List<GooglePlaceDetailed.Period> = java.util.ArrayList()) : Serializable

    /**
     * Contains the geocoded latitude, longitude value for this place.
     * @property lat latitude
     * @property lng longitude
     */
    data class GoogleLocation(
            val lat: Double = 0.0,
            val lng: Double = 0.0) : Serializable

    /**
     * @property open a pair of day and time objects describing when the place opens:
     * @property close may contain a pair of day and time objects describing when the place closes.
     */
    data class Period(
            val open: GooglePlaceDetailed.DayTime = GooglePlaceDetailed.DayTime(),
            val close: GooglePlaceDetailed.DayTime = GooglePlaceDetailed.DayTime()) : Serializable

    /**
     * @property photoReference — a string used to identify the photo when you perform a Photo request.
     * @property height — the maximum height of the image.
     * @property width — the maximum width of the image.
     * @property htmlAttributions — any required attributions. This field will always be present, but may be empty.
     */
    data class Photo(
            val width: Double = 0.0,
            val height: Double = 0.0,
            @JsonProperty("html_attributions") val htmlAttributions: List<String> = java.util.ArrayList(),
            @JsonProperty("photo_reference") var photoReference: String = "") : Serializable

    /**
     * @property addressComponents is an array of separate address components used to compose a given address.
     * @property formattedAddress is a string containing the human-readable address of this place.
     * @property formattedPhoneNumber the place's phone number in its local format.
     * @property geometry [Geometry]
     * @property icon the URL of a suggested icon which may be displayed to the user when indicating this result on a map.
     * @property id a unique stable identifier denoting this place.
     * @property internationalPhoneNumber the place's phone number in international format.
     * @property name the human-readable name for the returned result.
     * @property alternativeIds list of [AlternativeId]
     * @property openingHours [OpeningHours]
     * @property permanentlyClosed is a boolean flag indicating whether the place has permanently shut down (value true).
     * @property photos an array of [Photo] objects, each containing a reference to an image.
     * @property placeId textual identifier that uniquely identifies a place.
     * @property scope can contain value APP, if it available only for application. Otherwise, it can be used everywhere.
     * @property priceLevel the price level of the place, on a scale of 0 to 4 based on local prices.
     * @property rating the place's rating, from 1.0 to 5.0, based on aggregated user reviews.
     * @property reference a token that can be used to query the Details service in future.
     * @property reviews a JSON array of up to five [Review].
     * @property types an array of feature types describing the given result.
     * @property url the URL of the official Google page for this place.
     * @property utcOffset the number of minutes this place’s current timezone is offset from UTC.
     * @property vicinity lists a simplified address for the place, with the street name, street number, and locality.
     * @property website lists the authoritative website for this place, such as a business' homepage.
     */
    data class Result(
            val geometry: GooglePlaceDetailed.Geometry = GooglePlaceDetailed.Geometry(),
            @JsonProperty("opening_hours") val openingHours: GooglePlaceDetailed.OpeningHours = GooglePlaceDetailed.OpeningHours(),
            @JsonProperty("formatted_address") val formattedAddress: String = "",
            @JsonProperty("formatted_phone_number") val formattedPhoneNumber: String = "",
            @JsonProperty("international_phone_number") val internationalPhoneNumber: String = "",
            @JsonProperty("place_id") val placeId: String = "",
            val icon: String = "",
            val id: String = "",
            val name: String = "",
            val reference: String = "",
            val scope: String = "",
            val vicinity: String = "",
            val website: String = "",
            val url: String = "",
            val rating: Double = 0.0,
            @JsonProperty("permanently_closed") val permanentlyClosed: Boolean = false,
            @JsonProperty("utc_offset") val utcOffset: Int = 0,
            @JsonProperty("price_level") val priceLevel: Int = 0,
            val types: List<String> = java.util.ArrayList(),
            val photos: List<GooglePlaceDetailed.Photo> = java.util.ArrayList(),
            @JsonProperty("alt_ids") val alternativeIds: List<GooglePlaceDetailed.AlternativeId> = java.util.ArrayList(),
            @JsonProperty("address_components") val addressComponents: List<GooglePlaceDetailed.AddressComponent> = java.util.ArrayList(),
            val reviews: List<GooglePlaceDetailed.Review> = java.util.ArrayList()) : Serializable

    /**
     * @property aspects a collection of [Aspect] objects
     * @property authorUrl the URL to the users Google+ profile, if available.
     * @property authorName author's name, if available
     * @property language an IETF language code indicating the language used in the user's review.
     * @property rating the user's overall rating for this place. This is a whole number, ranging from 1 to 5.
     * @property text the user's review.
     * @property time the time that the review was submitted, measured in the number of seconds since since midnight, January 1, 1970 UTC.
     */
    data class Review(
            val aspects: List<GooglePlaceDetailed.Aspect> = java.util.ArrayList(),
            @JsonProperty("author_name") val authorName: String = "",
            @JsonProperty("author_url") val authorUrl: String = "",
            val language: String = "",
            val rating: Double = 0.0,
            val text: String = "",
            val time: Int = 0) : Serializable

    /**
     * Contains the preferred viewport when displaying this place on a map as a LatLngBounds if it is known.
     * @property northeast North east bound.
     * @property southwest South west bound.
     */
    data class Viewport(
            val northeast: GooglePlaceDetailed.GoogleLocation = GooglePlaceDetailed.GoogleLocation(),
            val southwest: GooglePlaceDetailed.GoogleLocation = GooglePlaceDetailed.GoogleLocation()) : Serializable
}
