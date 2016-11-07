package com.krev.trycrypt.model.entity

import com.krev.trycrypt.model.Id
import java.io.Serializable
import java.util.*


/**
 * This class is photo, that contains information about photo's id, owner id and uri.
 * @author      Dmitry Baynak
 * @version     0.0.1
 * @since       0.0.1
 * @property    id          Id
 * @property    location    where is geographically place located.
 * @property    allowed     collection of places, where user has access.
 * @property    created     collection of places, which are created by this user.
 * @property    friends     collection of users, who are user's friends.
 * @constructor defined place's properties.
 */

class User(override var id: Id = Id(0),
           var location: Location = Location(),
           var friends: Set<Id> = HashSet(),
           var created: Set<Id> = HashSet(),
           var allowed: Set<Id> = HashSet()) : BaseEntity(id), Serializable
