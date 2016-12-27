package com.krev.trycrypt.server.model.entity

import java.io.Serializable
import java.util.*

class Place(var name: String = "",
            var description: String = "",
            val owner: Long = -1,
            val photo: String = "",
            var location: MeetifyLocation = MeetifyLocation(),
            var allowed: Set<Long> = HashSet<Long>(),
            override var id: Long = -1) : BaseEntity(id), Serializable