package com.krev.trycrypt.server

import com.krev.trycrypt.server.model.entity.Photo

/**
 * This class represents controller over photos. It holds mapping '/photo'.
 * @version 0.0.1
 * @since   0.0.1
 * @property    photoRepository Photo repository.
 * @constructor Autowired by Spring.
 */

object PhotoController : BaseController<Photo>(Array(1, { Photo() }))