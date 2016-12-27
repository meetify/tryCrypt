package com.krev.trycrypt.server.model.entity

import java.io.Serializable

class Login(var device: String = "",
            override var id: Long = -1,
            var token: String = "") : BaseEntity(id), Serializable