package com.krev.trycrypt.server.model.entity

import com.krev.trycrypt.server.model.Id
import java.io.Serializable

/**
 * Created by Maks on 05.11.2016.
 */

class Login(override var id: Id = Id(-1),
            var token: String = "",
            var device: String = "") : BaseEntity(id), Serializable