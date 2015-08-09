package de.moso.repository

import org.springframework.data.mongodb.repository.MongoRepository
import de.moso.entity._

import collection.JavaConversions._
/**
 * Created by Sandro on 08.08.15 for shop.
 */
trait ProductRepository extends MongoRepository[Product, String] {

  //import collection.mutable._

  def findByName(name: String): java.util.List[Product]

}
