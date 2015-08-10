package de.moso.entity

import java.util.Date

import org.springframework.data.annotation.Id

/**
 * Created by Sandro on 08.08.15 for shop.
 */


case class Price ( price: Float
                    ) extends ShopEntity

case class Image ( path: String
                  ) extends ShopEntity

case class Product( name: String
                  , description: String
                  , productId: String
                  , inStock: Boolean
                  , price: Price
                  , image: Image
                    ) extends ShopDBEntity {
  @Id
  var id: java.lang.String = _

}

case class Comment ( productId: String
                   , comment: String
                   , rating: Int
                   , username: String
                   , date: Date
                     ) extends ShopDBEntity {
  @Id
  var id: java.lang.String = _

}
