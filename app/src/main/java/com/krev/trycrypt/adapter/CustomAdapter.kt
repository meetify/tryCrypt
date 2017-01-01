package com.krev.trycrypt.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.krev.trycrypt.application.Config.layoutInflater
import java.util.*

abstract class CustomAdapter<T>(open val items: MutableList<T> = ArrayList()) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(i: Int): T = items[i]

    fun add(list: Collection<T>) = apply {
        items.addAll(list).let { notifyDataSetChanged() }
    }

    fun clear(list: Collection<T> = ArrayList()) = apply {
        items.clear()
        add(list)
        notifyDataSetChanged()
    }

    override fun getItemId(i: Int): Long = 0

    @SuppressLint("ViewHolder") //it shouldn't be warned?
    override fun getView(position: Int, contentView: View?, viewGroup: ViewGroup) = (contentView ?:
            layoutInflater.inflate(layout, viewGroup, false).holder()).data(items[position])

    abstract fun photo(item: T, view: ImageView): Unit

    abstract fun View.holder(): View

    abstract fun View.data(item: T): View

    abstract val layout: Int
}
