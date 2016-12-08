package com.krev.trycrypt.adapters

import android.graphics.Bitmap
import android.support.annotation.LayoutRes
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.krev.trycrypt.application.Config
import com.krev.trycrypt.server.model.entity.BaseEntity
import com.krev.trycrypt.utils.async.ImageTask
import com.krev.trycrypt.utils.functional.Consumer
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Dima on 07.12.2016.
 */
abstract class CustomAdapter<T : BaseEntity>(
        val photos: ConcurrentHashMap<T, Bitmap> = ConcurrentHashMap<T, Bitmap>(),
        val items: MutableList<T> = ArrayList()) : BaseAdapter() {

    override fun getCount(): Int = photos.size

    override fun getItem(i: Int): T = items[i]

    fun add(list: Collection<T>) = list.forEach {
        items.add(it)
        photos.put(it, Config.bitmap)
        ImageTask(Consumer { bitmap ->
            photos.put(it, bitmap)
            notifyDataSetChanged()
        }, "user_${it.id.id}").execute(photo(it))
    }.let { this }

    override fun getItemId(i: Int): Long = 0

    override fun getView(position: Int, contentView: View?, viewGroup: ViewGroup) = (contentView ?:
            Config.layoutInflater.inflate(layout(), viewGroup, false)).apply {
        val item = items[position]
        tag = viewHolder(item)
    }!!

    abstract fun photo(item: T): String

    abstract fun View.viewHolder(item: T): Any

    abstract @LayoutRes fun layout(): Int
}