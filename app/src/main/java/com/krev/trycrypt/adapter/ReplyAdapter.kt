package com.krev.trycrypt.adapter

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.krev.trycrypt.R
import com.krev.trycrypt.model.Reply
import com.krev.trycrypt.util.PhotoUtils

/**
 * Created by Dima on 15.01.2017.
 */
class ReplyAdapter : CustomAdapter<Reply>() {
    override fun View.holder() = apply {
        tag = ViewHolder(
                (findViewById(R.id.listview_reply_image) as ImageView),
                (findViewById(R.id.listview_reply_author) as TextView),
                (findViewById(R.id.listview_reply_comment) as EditText),
                (findViewById(R.id.listview_reply_likes_image) as ImageView),
                (findViewById(R.id.listview_reply_likes_count) as TextView))
    }

    override fun View.data(item: Reply) = apply {
        (tag as ViewHolder).apply {
            if (item.type == Reply.Type.GOOGLE) {
                image.setImageResource(R.drawable.ic_person)
                likes.setImageResource(R.drawable.ic_stars)
            } else {
                photo(item, image)
                likes.setImageResource(R.drawable.ic_like)
            }
            author.text = item.author
            comment.text.insert(0, item.reply.trim(*chars))
            comment.setSelection(0)
            likesCount.text = item.ratingOrLikes
        }
    }

    override val layout: Int = R.layout.listview_reply

    override fun photo(item: Reply, view: ImageView) {
        PhotoUtils.get(item.tag as String, item.author, view)
    }

    class ViewHolder(val image: ImageView,
                     val author: TextView,
                     val comment: EditText,
                     val likes: ImageView,
                     val likesCount: TextView)

    companion object {
        val chars = charArrayOf(' ', '\n', '\r', '\t')
    }
}