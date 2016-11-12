package com.krev.trycrypt.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.Serializable
import java.util.*

/**
 * Google place representation. All documentation about this class & inner
 * classes was taken from [Google documentation](https://developers.google.com/places/web-service/details)
 * and it's licensed under the [CC BY 3.0](https://creativecommons.org/licenses/by/3.0/).
 * No changes were made, except of some in-page redirects.
 * @property results           contains the detailed information about the place requested.
 * @property status            contains metadata on the request.
 * @property htmlAttributions  contains a set of attributions about this listing which must be displayed to the user.
 * @property nextPageToken     contains a token which can be used to access some more places instead of default 20.
 * @author      Dmitry Baynak
 * @version     0.0.1
 * @since       0.0.1
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class GooglePlace(
        var results: List<Result> = ArrayList(),
        var status: String = "",
        @JsonProperty("html_attributions") var htmlAttributions: List<String> = ArrayList(),
        @JsonProperty("next_page_token") var nextPageToken: String = "") : Serializable {

    /**
     * @property types      is an array indicating the type of the address component.
     * @property longName   is the full text description or name of the address component.
     * @property shortName  is an abbreviated textual name for the address component, if available.
     */
    data class AddressComponent(
            @JsonProperty("long_name") var longName: String = "",
            @JsonProperty("short_name") var shortName: String = "",
            var types: List<String>? = ArrayList()) : Serializable

    /**
     * @property placeId the most likely reason for a place to have an alternative place ID is if your application adds a place and receives an application-scoped place ID, then later receives a Google-scoped place ID after passing the moderation process.
     * @property scope   the scope of an alternative place ID will always be APP, indicating that the alternative place ID is recognised by your application only.
     */
    data class AlternativeId(
            @JsonProperty("place_id") var placeId: String = "",
            var scope: String = "") : Serializable

    /**
     * @property type   the name of the aspect that is being rated. The following types are supported: appeal, atmosphere, decor, facilities, food, overall, quality and service.
     * @property rating the user's rating for this particular aspect, from 0 to 3.
     */
    data class Aspect(
            var rating: Int = 0,
            var type: String = "") : Serializable

    /**
     * @property day    a number from 0–6, corresponding to the days of the week, starting on Sunday. For example, 2 means Tuesday.
     * @property time   may contain a time of day in 24-hour hhmm format. Values are in the range 0000–2359. The time will be reported in the place’s time zone.
     */
    data class DayTime(
            var day: Int = 0,
            var time: Int = 0) : Serializable

    /**
     * @property location contains the geocoded latitude,longitude value for this place.
     * @property viewport contains the preferred viewport when displaying this place on a map as a LatLngBounds if it is known.
     */
    data class Geometry(
            var location: GoogleLocation = GoogleLocation(),
            var viewport: Viewport = Viewport()) : Serializable

    /**
     * @property openNow is a boolean value indicating if the place is open at the current time.
     * @property periods is an array of opening periods covering seven days, starting from Sunday, in chronological order. Each period contains:
     * @property weekdayText is an array of seven strings representing the formatted opening hours for each day of the week.
     */
    data class OpeningHours(
            @JsonProperty("open_now") var openNow: Boolean = false,
            @JsonProperty("weekday_text") var weekdayText: List<String> = ArrayList(),
            var periods: List<Period> = ArrayList()) : Serializable

    /**
     * Contains the geocoded latitude, longitude value for this place.
     * @property lat latitude
     * @property lng longitude
     */
    data class GoogleLocation(
            var lat: Double = 0.0,
            var lng: Double = 0.0) : Serializable

    /**
     * @property open contains a pair of day and time objects describing when the place opens:
     * @property close may contain a pair of day and time objects describing when the place closes.
     */
    data class Period(
            var open: DayTime = DayTime(),
            var close: DayTime = DayTime()) : Serializable

    /**
     * @property photoReference — a string used to identify the photo when you perform a Photo request.
     * @property height — the maximum height of the image.
     * @property width — the maximum width of the image.
     * @property htmlAttributions — contains any required attributions. This field will always be present, but may be empty.
     */
    data class Photo(
            var width: Double? = 0.0,
            var height: Double = 0.0,
            @JsonProperty("html_attributions") var htmlAttributions: List<String> = ArrayList(),
            @JsonProperty("photo_reference") var photoReference: String = "") : Serializable

    /**
     * @property addressComponents is an array of separate address components used to compose a given address.
     * @property formattedAddress is a string containing the human-readable address of this place.
     * @property formattedPhoneNumber contains the place's phone number in its local format.
     * @property geometry [Geometry]
     * @property icon contains the URL of a suggested icon which may be displayed to the user when indicating this result on a map.
     * @property id contains a unique stable identifier denoting this place.
     * @property internationalPhoneNumber contains the place's phone number in international format.
     * @property name contains the human-readable name for the returned result.
     * @property alternativeIds list of [AlternativeId]
     * @property openingHours [OpeningHours]
     * @property permanentlyClosed is a boolean flag indicating whether the place has permanently shut down (value true).
     * @property photos an array of [Photo] objects, each containing a reference to an image.
     * @property placeId textual identifier that uniquely identifies a place.
     * @property scope can contain value APP, if it available only for application. Otherwise, it can be used everywhere.
     * @property priceLevel the price level of the place, on a scale of 0 to 4 based on local prices.
     * @property rating contains the place's rating, from 1.0 to 5.0, based on aggregated user reviews.
     * @property reference contains a token that can be used to query the Details service in future.
     * @property reviews a JSON array of up to five [Review].
     * @property types contains an array of feature types describing the given result.
     * @property url contains the URL of the official Google page for this place.
     * @property utcOffset contains the number of minutes this place’s current timezone is offset from UTC.
     * @property vicinity lists a simplified address for the place, with the street name, street number, and locality.
     * @property website lists the authoritative website for this place, such as a business' homepage.
     */
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

    /**
     * @property aspects contains a collection of [Aspect] objects
     * @property authorUrl the URL to the users Google+ profile, if available.
     * @property authorName author's name, if available
     * @property language an IETF language code indicating the language used in the user's review.
     * @property rating the user's overall rating for this place. This is a whole number, ranging from 1 to 5.
     * @property text the user's review.
     * @property time the time that the review was submitted, measured in the number of seconds since since midnight, January 1, 1970 UTC.
     */
    data class Review(
            var aspects: List<Aspect> = ArrayList(),
            @JsonProperty("author_name") var authorName: String = "",
            @JsonProperty("author_url") var authorUrl: String = "",
            var language: String = "",
            var rating: Double = 0.0,
            var text: String = "",
            var time: Int = 0) : Serializable

    /**
     * Contains the preferred viewport when displaying this place on a map as a LatLngBounds if it is known.
     * @property northeast North east bound.
     * @property southwest South west bound.
     */
    data class Viewport(
            var northeast: GoogleLocation = GoogleLocation(),
            var southwest: GoogleLocation = GoogleLocation()) : Serializable
    companion object{
        val mapper = jacksonObjectMapper()
    }
}
