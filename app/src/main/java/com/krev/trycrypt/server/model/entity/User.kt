package com.krev.trycrypt.server.model.entity

import java.io.Serializable
import java.util.*


/**
 * This class is photo, that contains information about photo's id, owner id and uri.
 * @version     0.0.1
 * @since       0.0.1
 * @property    id          Id
 * @property    location    where is geographically place located.
 * @property    allowed     collection of places, where user has access.
 * @property    created     collection of places, which are created by this user.
 * @property    friends     collection of users, who are user's friends.
 * @constructor defined place's properties.
 */
class User(override var id: Long = -1,
           var location: MeetifyLocation = MeetifyLocation(),
           var friends: Set<Long> = HashSet(),
           var created: Set<Long> = HashSet(),
           var allowed: Set<Long> = HashSet(),
           var name: String = "",
           var photo: String = "",
           var time: Long = 0,
           var vkAlbum: Long = 0
) : BaseEntity(id), Serializable
