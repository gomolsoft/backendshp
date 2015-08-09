package de.moso.entity

import java.time.LocalDateTime

import org.springframework.data.annotation.Id

/**
 * Created by Sandro on 08.08.15 for shop.
 */


case class Price ( price: Float
                    )

case class Image ( path: String
                  )

case class Product( name: String
                  , description: String
                  , productId: String
                  , inStock: Boolean
                  , price: Price
                  , image: Image
                    ) {
  @Id
  var id: java.lang.String = _

}

case class Comment ( productId: String
                   , comment: String
                   , rating: Int
                   , username: String
                   , date: LocalDateTime
                     ) {
  @Id
  var id: java.lang.String = _

}
