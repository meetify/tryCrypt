package com.krev.trycrypt.utils.async

import android.os.AsyncTask
import com.krev.trycrypt.utils.Consumer
import com.krev.trycrypt.utils.Supplier

/**
 * Created by Dima on 14.11.2016.
 */
class Task<V>(private var supplier: Supplier<out V>,
              private var consumer: Consumer<in V>) : AsyncTask<Any?, Any?, Any?>() {
    override fun doInBackground(vararg p0: Any?) = consumer.accept(supplier.accept())
}