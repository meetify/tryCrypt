package com.krev.trycrypt.utils.async

import android.os.AsyncTask
import com.krev.trycrypt.utils.Consumer
import com.krev.trycrypt.utils.Supplier

/**
 * Created by Dima on 14.11.2016.
 */
class Task<V>(private var supplier: Supplier<V>,
              private var consumer: Consumer<V>) : AsyncTask<Void?, Void?, Void?>() {
    override fun doInBackground(vararg p0: Void?): Nothing? {
        return consumer.accept(supplier.accept()).let { null }
    }
}