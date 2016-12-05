package com.krev.trycrypt.server.model.entity

import com.krev.trycrypt.server.model.Id
import java.io.Serializable
import java.util.*

class User(override var id: Id = Id(0),
           var location: MeetifyLocation = MeetifyLocation(),
           var friends: Set<Id> = HashSet(),
           var created: Set<Id> = HashSet(),
           var allowed: Set<Id> = HashSet(),
           var name: String = "",
           var photo: String = "",
           var time: Long = 0) : BaseEntity(id), Serializable {
    //maybe not be used
    fun modify(user: User) {
        this.id = user.id
        this.location = user.location
        this.friends = user.friends
        this.created = user.created
        this.allowed = user.allowed
        this.name = user.name
        this.photo = user.photo
        this.time = user.time
    }
}
