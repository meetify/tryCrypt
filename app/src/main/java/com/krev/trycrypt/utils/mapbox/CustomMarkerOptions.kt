package com.krev.trycrypt.utils.mapbox

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.krev.trycrypt.activity.MapActivity
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.server.model.GooglePlace
import com.krev.trycrypt.utils.TypeMapper
import com.mapbox.mapboxsdk.annotations.BaseMarkerOptions
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.geometry.LatLng

/**
 * Created by Dima on 06.12.2016.
 */
class CustomMarkerOptions() : BaseMarkerOptions<CustomMarker, CustomMarkerOptions>() {
    constructor(place: GooglePlace.Result) : this() {
        position = MapActivity.convert(place.geometry.location)
        title = place.name
        marker.place = place
        icon = IconFactory.getInstance(Config.context).fromDrawable(
                Config.context.getDrawable(TypeMapper.drawable(place.types)))
    }

    constructor(input: Parcel) : this() {
        marker = CustomMarker()
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