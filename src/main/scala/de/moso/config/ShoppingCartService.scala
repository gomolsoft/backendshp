package de.moso.config

import de.moso.controller.ApiCart
import net.sf.ehcache.{CacheManager, Element}
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

/**
 * Created by Sandro on 11.08.15 for shop.
 */

@Service
class ShoppingCartService {
  val logger = LoggerFactory.getLogger(classOf[TokenService])
  val restShopApiAuthTokenCache = CacheManager.getInstance.getCache("restShopApiShopCartCache")

  @Scheduled(fixedRate =  3 * 60 * 1000)
  def evictExpiredTokens {
    logger.info("Evicting Shoping tokens")
    restShopApiAuthTokenCache.evictExpiredElements
  }

  def store(token: String, apiCart: ApiCart): ApiCart = {
    restShopApiAuthTokenCache.put(new Element(token, apiCart))
    apiCart
  }

  def contains(token: String): Boolean = {
    restShopApiAuthTokenCache.get(token) != null
  }

  def retrieve(token: String): Option[ApiCart] = {
    if (contains(token))
      return Some(restShopApiAuthTokenCache.get(token).getObjectValue.asInstanceOf[ApiCart])

    None
  }
}
