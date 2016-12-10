package com.krev.trycrypt.server.model.entity

import java.io.Serializable
import java.util.*

/**
 * This class is place, that contains information place.
 * @version     0.0.1
 * @since       0.0.1
 * @property    name        place name.
 * @property    description place description.
 * @property    id          Id.
 * @property    owner       owner id.
 * @property    photo       photo.
 * @property    location    where is geographically place located.
 * @property    allowed     collection with users, which can view info about this place.
 * @constructor defined place's properties.
 */
class Place(var name: String = "",
            var description: String = "",
            val owner: Long = -1,
            val photo: String = "",
            var location: MeetifyLocation = MeetifyLocation(),
            var allowed: Set<Long> = HashSet<Long>(),
            override var id: Long = -1) : BaseEntity(id), Serializable