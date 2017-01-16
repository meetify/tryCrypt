package com.krev.trycrypt.model

import java.io.Serializable

/**
 * Created by Dima on 15.01.2017.
 */
data class Reply(val author: String = "",
                 val reply: String = "",
                 val tag: Any = Unit,
                 val ratingOrLikes: String = "",
                 val type: Type = Type.MEETIFY) : Serializable {

    enum class Type {
        GOOGLE, MEETIFY
    }

    companion object Builder {
        fun from(review: GooglePlaceDetailed.Review) = Reply(
                review.authorName,
                review.text,
                "",
                review.rating.toString(),
                Type.GOOGLE
        )
    }
}