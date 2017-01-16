package com.krev.trycrypt.util.mapbox

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.krev.trycrypt.R
import com.krev.trycrypt.activity.MapActivity
import com.krev.trycrypt.model.Config
import com.krev.trycrypt.model.GooglePlace
import com.krev.trycrypt.model.Viewable
import com.krev.trycrypt.model.entity.Place
import com.krev.trycrypt.util.TypeMapper
import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.geometry.LatLng

class CustomMarkerOptions internal constructor(val tag: Viewable) : BaseMarkerOptions<CustomMarker, CustomMarkerOptions>() {

    constructor(input: Parcel) : this(Place()) {
        marker = CustomMarker(this)
        position(input.readParcelable<Parcelable>(LatLng::class.java.classLoader) as LatLng)
        snippet(input.readString())
        title(input.readString())
        if (input.readByte().toInt() != 0) {
            // this means we have an icon
            val iconId = input.readString()
            val iconBitmap = input.readParcelable<Bitmap>(Bitmap::class.java.classLoader)
            val icon = IconFactory.recreate(iconId, iconBitmap)
            icon(icon)
        }
    }

    companion object Builder {
        fun from(place: GooglePlace.Result) = CustomMarkerOptions(place).apply {
            position = MapActivity.convert(place.geometry.location)
            title = place.name
            icon = IconFactory.getInstance(Config.context).fromResource(TypeMapper.drawable(place.types))
        }

        fun from(place: Place) = CustomMarkerOptions(place).apply({
            title = place.name
            position = MapActivity.convert(place.location)
            icon = IconFactory.getInstance(Config.context).fromResource(R.drawable.ic_place_custom)
        })
    }

    val CREATOR: Parcelable.Creator<CustomMarkerOptions> = object : Parcelable.Creator<CustomMarkerOptions> {
        override fun createFromParcel(input: Parcel): CustomMarkerOptions {
            return CustomMarkerOptions(input)
        }

        override fun newArray(size: Int): Array<CustomMarkerOptions?> {
            return arrayOfNulls(size)
        }
    }

    private var marker: CustomMarker = CustomMarker(this)

    override fun getThis(): CustomMarkerOptions = this

    override fun getMarker(): CustomMarker {
        marker.position = position
        marker.snippet = snippet
        marker.title = title
        marker.icon = icon
        return marker
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeParcelable(position, flags)
        out.writeString(snippet)
        out.writeString(title)
        val icon = icon
        out.writeByte((if (icon != null) 1 else 0).toByte())
        if (icon != null) {
            out.writeString(icon.id)
            out.writeParcelable(icon.bitmap, flags)
        }
    }
}