package com.krev.trycrypt.model

import com.krev.trycrypt.model.entity.Place
import com.krev.trycrypt.model.entity.User
import java.util.*

data class UserExtended(var friends: Set<User> = HashSet(),
                        var created: Set<Place> = HashSet(),
                        var allowed: Set<Place> = HashSet())