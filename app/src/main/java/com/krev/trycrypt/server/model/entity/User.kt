package com.krev.trycrypt.server.model.entity

import java.io.Serializable
import java.util.*

class User(override var id: Long = -1,
           var location: MeetifyLocation = MeetifyLocation(),
           var friends: Set<Long> = HashSet(),
           var created: Set<Long> = HashSet(),
           var allowed: Set<Long> = HashSet(),
           var name: String = "",
           var photo: String = "",
           var time: Long = 0,
           var vkAlbum: Long = 0) : BaseEntity(id), Serializable
