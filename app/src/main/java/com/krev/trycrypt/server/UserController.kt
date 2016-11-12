package com.krev.trycrypt.server

import com.krev.trycrypt.asynctasks.Consumer
import com.krev.trycrypt.asynctasks.Supplier
import com.krev.trycrypt.model.entity.User
import okhttp3.Request

/**
 * This class represents controller over users. It holds mapping '/user'.
 * @version 0.0.1
 * @since   0.0.1
 * @property   userRepository  users repository, which is provided by Spring & Hibernate.
 * @constructor             Autowired by Spring.
 */
object UserController : BaseController<User>(Array(1, { User() })) {
    fun friends(consumer: Consumer<List<User>>) {
        Task(Supplier {
            mapper.readValue(client.newCall(Request.Builder()
                    .url(url() + "/friends")
                    .get().build()).execute().body().string(), array.javaClass).asList()
        }, consumer).execute()
    }
}