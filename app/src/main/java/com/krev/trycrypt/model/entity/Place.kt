package com.krev.trycrypt.model.entity

import com.krev.trycrypt.model.Id
import java.io.Serializable
import java.util.*


/**
 * This class is place, that contains information place.
 * @author      Dmitry Baynak
 * @version     0.0.1
 * @since       0.0.1
 * @property    name        place name.
 * @property    description place description.
 * @property    id          Id.
 * @property    owner       owner id.
 * @property    photo       photo id.
 * @property    location    where is geographically place located.
 * @property    allowed     collection with users, which can view info about this place.
 * @constructor defined place's properties.
 */

class Place(var name: String = "",
            var description: String = "",
            val owner: Id = Id(-1),
            val photo: Id = Id(-1),
            override var id: Id = Id(-1),
            var allowed: Set<Id> = HashSet<Id>(),
            var location: Location = Location()) : BaseEntity(id), Serializable