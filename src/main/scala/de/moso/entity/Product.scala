package de.moso.entity

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
                  , inStock: Boolean
                  , price: Price
                  , image: Image
                    ) {
  @Id
  var id: java.lang.String = _

}