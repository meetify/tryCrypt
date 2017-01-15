package com.krev.trycrypt.model

import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.krev.trycrypt.R
import com.krev.trycrypt.activity.MapActivity
import com.krev.trycrypt.adapter.ReplyAdapter
import com.krev.trycrypt.server.PlaceController
import com.krev.trycrypt.util.BitmapUtils
import com.krev.trycrypt.util.PhotoUtils
import com.krev.trycrypt.util.TypeMapper
import java.io.Serializable
import java.util.*

/**
 * Google place representation.
 * All documentation about this class & inner classes was taken from
 * [Google documentation](https://developers.google.com/places/web-service/details)
 * and it's licensed under the [CC BY 3.0](https://creativecommons.org/licenses/by/3.0/).
 * No changes were made, except of some in-page redirects.
 * @property results      the detailed information about the place requested.
 * @property status      metadata on the request.
 * @property htmlAttributions a set of attributions about this listing which must be displayed to the user.
 * @property nextPageToken   a token which can be used to access some more places instead of default 20.
 * @since    0.1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class GooglePlace(
        var results: List<com.krev.trycrypt.model.GooglePlace.Result> = ArrayList(),
        val status: String = "",
        @JsonProperty("html_attributions") val htmlAttributions: List<String> = ArrayList(),
        @JsonProperty("next_page_token") val nextPageToken: String = "") : Serializable {
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
            val location: com.krev.trycrypt.model.GooglePlace.GoogleLocation = com.krev.trycrypt.model.GooglePlace.GoogleLocation(),
            val viewport: com.krev.trycrypt.model.GooglePlace.Viewport = com.krev.trycrypt.model.GooglePlace.Viewport()) : Serializable

    /**
     * @property openNow is a boolean value indicating if the place is open at the current time.
     * @property periods is an array of opening periods covering seven days, starting from Sunday, in chronological order.
     * @property weekdayText is an array of seven strings representing the formatted opening hours for each day of the week.
     */
    data class OpeningHours(
            @JsonProperty("open_now") val openNow: Boolean = false,
            @JsonProperty("weekday_text") val weekdayText: List<String> = ArrayList(),
            val periods: List<com.krev.trycrypt.model.GooglePlace.Period> = ArrayList()) : Serializable

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
            val open: com.krev.trycrypt.model.GooglePlace.DayTime = com.krev.trycrypt.model.GooglePlace.DayTime(),
            val close: com.krev.trycrypt.model.GooglePlace.DayTime = com.krev.trycrypt.model.GooglePlace.DayTime()) : Serializable

    /**
     * @property photoReference — a string used to identify the photo when you perform a Photo request.
     * @property height — the maximum height of the image.
     * @property width — the maximum width of the image.
     * @property htmlAttributions — any required attributions. This field will always be present, but may be empty.
     */
    data class Photo(
            val width: Double = 0.0,
            val height: Double = 0.0,
            @JsonProperty("html_attributions") val htmlAttributions: List<String> = ArrayList(),
            @JsonProperty("photo_reference") var photoReference: String = "") : Serializable

    /**
     * @property formattedAddress is a string containing the human-readable address of this place.
     * @property formattedPhoneNumber the place's phone number in its local format.
     * @property geometry [Geometry]
     * @property icon the URL of a suggested icon which may be displayed to the user when indicating this result on a map.
     * @property id a unique stable identifier denoting this place.
     * @property internationalPhoneNumber the place's phone number in international format.
     * @property name the human-readable name for the returned result.
     * @property openingHours [OpeningHours]
     * @property permanentlyClosed is a boolean flag indicating whether the place has permanently shut down (value true).
     * @property photos an array of [Photo] objects, each containing a reference to an image.
     * @property placeId textual identifier that uniquely identifies a place.
     * @property scope can contain value APP, if it available only for application. Otherwise, it can be used everywhere.
     * @property priceLevel the price level of the place, on a scale of 0 to 4 based on local prices.
     * @property rating the place's rating, from 1.0 to 5.0, based on aggregated user reviews.
     * @property reference a token that can be used to query the Details service in future.
     * @property types an array of feature types describing the given result.
     * @property url the URL of the official Google page for this place.
     * @property utcOffset the number of minutes this place’s current timezone is offset from UTC.
     * @property vicinity lists a simplified address for the place, with the street name, street number, and locality.
     * @property website lists the authoritative website for this place, such as a business' homepage.
     */
    data class Result(
            val geometry: com.krev.trycrypt.model.GooglePlace.Geometry = com.krev.trycrypt.model.GooglePlace.Geometry(),
            @JsonProperty("opening_hours") val openingHours: com.krev.trycrypt.model.GooglePlace.OpeningHours = com.krev.trycrypt.model.GooglePlace.OpeningHours(),
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
            val types: List<String> = ArrayList(),
            val photos: List<com.krev.trycrypt.model.GooglePlace.Photo> = ArrayList()) : Serializable, Viewable {

        override fun MapActivity.showDialog(builder: AlertDialog.Builder) {
            val view = layoutInflater.inflate(R.layout.dialog_information_place, null)

            val photo = view.findViewById(R.id.place_info_image) as ImageView
            val location = view.findViewById(R.id.place_info_location_text) as TextView
            val creator = view.findViewById(R.id.place_info_creator_text) as TextView
            val allowed = view.findViewById(R.id.place_info_friends_text) as TextView
            val time = view.findViewById(R.id.place_info_time_text) as TextView
            val name = view.findViewById(R.id.place_info_name_text) as TextView
            val allowedImage = view.findViewById(R.id.place_info_friends_image) as ImageView
            val timeImage = view.findViewById(R.id.place_info_time_image) as ImageView
            val creatorImage = view.findViewById(R.id.place_info_creator_image) as ImageView

            val listView = view.findViewById(R.id.place_info_time_listview) as ListView
            val adapter = ReplyAdapter()
            listView.adapter = adapter

            val stringBuilder = StringBuilder()
            PlaceController.details(placeId).thenApplyAsync {
                runOnUiThread {
                    longTask {
                        @Suppress("NAME_SHADOWING")
                        val place = it.result
                        val (image, text) = when {
                            place.website.isNotBlank() -> R.drawable.ic_web.to(place.website)
                            place.internationalPhoneNumber.isNotBlank() -> R.drawable.ic_phone.to(place.internationalPhoneNumber)
                            place.formattedPhoneNumber.isNotBlank() -> R.drawable.ic_phone.to(place.formattedPhoneNumber)
                            else -> R.drawable.ic_empty.to("")
                        }
                        name.text = place.name
                        creator.text = text
                        creatorImage.setImageResource(image)
                        location.text = place.vicinity


//                image.setImageResource()
                        allowedImage.setImageResource(TypeMapper.drawable(place.types))
                        if (place.rating > 1.0) {
                            time.text = place.rating.toString()
                            timeImage.setImageResource(R.drawable.ic_stars)
                        } else {
                            timeImage.visibility = View.INVISIBLE
                            time.text = ""
                        }
                        val items = place.reviews.filter { it.text.trim(*ReplyAdapter.chars) != "" }
                                .map(Reply.Builder::from)
                        val pixels = BitmapUtils.toPixels(100f)
                        val size = when (items.count()) {
                            in 0..2 -> pixels * items.count()
                            else -> pixels * 2
                        }
                        adapter.add(items)
                        listView.layoutParams.height = size
                        listView.layoutParams.width = size

                        place.types.forEach { stringBuilder.append("${it.replace('_', ' ').capitalize()}, ") }
                        allowed.text = stringBuilder.toString().trim(' ').trim(',')

                        val aspectRatioPhoto = photo.width.toDouble() / photo.height.toDouble()

                        PhotoUtils.get(place.photos.minBy {
                            val aspectRatioIt = it.width / it.height
                            Math.abs(aspectRatioPhoto - aspectRatioIt)
                        }!!.photoReference, place.id, photo, round = false)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                        rotate.stop()
                    }
                }
            }
        }
    }

    /**
     * Contains the preferred viewport when displaying this place on a map as a LatLngBounds if it is known.
     * @property northeast North east bound.
     * @property southwest South west bound.
     */
    data class Viewport(
            val northeast: com.krev.trycrypt.model.GooglePlace.GoogleLocation = com.krev.trycrypt.model.GooglePlace.GoogleLocation(),
            val southwest: com.krev.trycrypt.model.GooglePlace.GoogleLocation = com.krev.trycrypt.model.GooglePlace.GoogleLocation()) : Serializable
}
