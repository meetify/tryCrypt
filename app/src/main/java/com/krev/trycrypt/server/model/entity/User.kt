package com.krev.trycrypt.server.model.entity

import com.krev.trycrypt.server.model.Id
import java.io.Serializable
import java.util.*

class User(override var id: Id = Id(0),
           var location: MeetifyLocation = MeetifyLocation(),
           var friends: Set<Id> = HashSet(),
           var created: Set<Id> = HashSet(),
           var allowed: Set<Id> = HashSet(),
           val name: String = "",
           val photo: String = "") : BaseEntity(id), Serializable
