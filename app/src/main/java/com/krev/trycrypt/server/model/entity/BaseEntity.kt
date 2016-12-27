package com.krev.trycrypt.server.model.entity

abstract class BaseEntity(open var id: Long = 0) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this.javaClass !== other.javaClass) return false
        return id == (other as BaseEntity).id
    }

    override fun hashCode() = id.hashCode()
}