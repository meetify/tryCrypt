package com.krev.trycrypt.server.model

import com.krev.trycrypt.server.model.entity.Place
import com.krev.trycrypt.server.model.entity.User
import java.util.*

data class UserExtended(var friends: Set<User> = HashSet(),
                        var created: Set<Place> = HashSet(),
                        var allowed: Set<Place> = HashSet())