package de.moso.repository

import de.moso.entity._
import org.springframework.data.mongodb.repository.MongoRepository
/**
 * Created by Sandro on 08.08.15 for shop.
 */
trait ProductRepository extends MongoRepository[Product, String] {
  def findByName(name: String): java.util.List[Product]

}

trait ProductCommentRepository extends MongoRepository[Comment, String] {
  def findByProductId(productId: String): java.util.List[Comment]

}
