package com.krev.trycrypt.server.model.entity

import java.io.Serializable

/**
 * Created by Maks on 05.11.2016.
 */

class Login(var device: String = "",
            override var id: Long = -1,
            var token: String = "") : BaseEntity(id), Serializable