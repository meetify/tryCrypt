package com.krev.trycrypt.server.model

import com.krev.trycrypt.server.model.entity.BaseEntity
import java.io.Serializable

/**
 * This class is embedded id for all entity classes. It's expressed in [BaseEntity] which contains instance of Id.
 * @author      Dmitry Baynak
 * @version     0.0.1
 * @since       0.0.1
 * @property    id  id
 * @constructor takes [id].
 */
data class Id(var id: Long = 0) : Serializable
