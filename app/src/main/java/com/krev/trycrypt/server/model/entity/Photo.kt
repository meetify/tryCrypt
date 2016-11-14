package com.krev.trycrypt.server.model.entity

import com.krev.trycrypt.server.model.Id
import java.io.Serializable

/**
 * This class is photo, that contains information about photo's id, owner id and uri.
 * @author      Dmitry Baynak
 * @version     0.0.1
 * @since       0.0.1
 * @property    id      Id
 * @property    owner   owner id
 * @property    uri     where is photo located (it can be as file:/// or http(s)://)
 * @constructor defined photo's properties.
 */

class Photo(
        override var id: Id = Id(),
        var owner: Id = Id(),
        var uri: String = "") : BaseEntity(id), Serializable