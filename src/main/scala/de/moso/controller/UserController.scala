package de.moso.controller

import de.moso.config.ShoppingCartService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{RequestMapping, RestController}

/**
 * Created by Sandro on 12.08.15 for shop.
 */
@RestController
@RequestMapping(Array("/user"))
class UserController {
  @Autowired var shopingCartService: ShoppingCartService = _
}
