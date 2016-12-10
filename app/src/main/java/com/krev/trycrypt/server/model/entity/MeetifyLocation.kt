package com.krev.trycrypt.server.model.entity

import java.io.Serializable

/**
 * This class is embedded location, that represents geographic coordinates of some object.
 * @version     0.0.1
 * @since       0.0.1
 * @property    latitude  latitude
 * @property    longitude  longitude
 * @constructor takes 2 doubles.
 */
data class MeetifyLocation(var latitude: Double = 0.0, var longitude: Double = 0.0) : Serializable