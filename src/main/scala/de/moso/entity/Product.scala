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

case class Discount ( productId: String
                    , discountPrice: Price
                    , discountPercent: Int
                    , validFrom: Date
                    , validTo: Date
                      ) extends ShopDBEntity {
  @Id
  var id: java.lang.String = _

}

case class ProductDescription(
                    shortDescription: String
                  , longDescription: String
                  , technicalDescription: String
                               )

case class Product( name: String
                  , productId: String
                  , productDescription: ProductDescription
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

object Discount {
  def emtyDiscount = Discount(null,Price(Float.NaN),0,null,null)
}