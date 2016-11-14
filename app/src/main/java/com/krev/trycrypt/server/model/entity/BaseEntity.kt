package com.krev.trycrypt.server.model.entity

import com.krev.trycrypt.server.model.Id

/**
 * This class is used to be extended by classes, which has Id and can be accessed by this Id.
 * @author      Dmitry Baynak
 * @version     0.0.1
 * @property    id  id
 * @since       0.0.1
 */
abstract class BaseEntity(open var id: Id = Id()) {

    /**
     * Equals method, that is overridden from Kotlin's Any.
     * @param   other   some object, with which should be compared current object using value of [id]
     * @return  result of comparing
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this.javaClass !== other.javaClass) return false
        return id == (other as BaseEntity).id
    }

    /**
     * Hashcode method, that is overridden from Kotlin's Any.
     * @return  hashcode of id.
     */
    override fun hashCode() = id.hashCode()
}