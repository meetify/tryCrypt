package com.krev.trycrypt.server

import android.os.AsyncTask
import com.krev.trycrypt.asynctasks.Consumer
import com.krev.trycrypt.asynctasks.Supplier

/**
 * Created by Dima on 14.11.2016.
 */
class Task<V>(private var supplier: Supplier<V>,
              private var consumer: Consumer<V>?) : AsyncTask<Void?, Void?, Void?>() {
    override fun doInBackground(vararg p0: Void?): Nothing? {
        return consumer?.accept(supplier.accept()).let { null }
    }
}