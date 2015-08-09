package de.moso.controller

import java.time.LocalDateTime
import java.util
import java.util.Calendar

import de.moso.entity.{Comment, Image, Price, Product}
import de.moso.repository.{ProductCommentRepository, ProductRepository}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

import scala.beans.BeanProperty

/**
 * Created by sandro on 27.05.15.
 */

import collection.JavaConversions._


case class ApiProduct( @BeanProperty var name: String
                     , @BeanProperty var description: String
                     , @BeanProperty var img: String
                     , @BeanProperty var inStock: Boolean
                     , @BeanProperty var price: Float
                     , @BeanProperty var comments: java.util.List[ApiProductComment]
                       )

case class ApiProductComment(
                         @BeanProperty var comment: String
                       , @BeanProperty var user: String
                       , @BeanProperty var rating: Int
                       )


@RestController
@RequestMapping(Array("/product"))
class ProductController {
  @Autowired var productRepository: ProductRepository = _
  @Autowired var commentRepository: ProductCommentRepository = _

  val build = (pt: Product) => {

    val i = if (pt.image==null) "" else pt.image.path
    val p = if (pt.price==null) -1.0f else pt.price.price

    val coms = commentRepository.findByProductId(pt.productId).map( (c: Comment) => build(c))
    ApiProduct(pt.name,pt.description,i,pt.inStock,p, coms)
  }

  //@PreAuthorize("hasRole('ROLE_DOMAIN_USER')")
  @RequestMapping(value = Array("/products"), produces = Array("application/json"), method = Array(RequestMethod.GET))
  def allDevices(): util.List[ApiProduct] = {
    val products = productRepository.findAll()
    products.map( (p:Product) => build(p))

  }

  //@PreAuthorize("hasRole('ROLE_DOMAIN_USER')")
  @RequestMapping(value = Array("/test"), produces = Array("application/json"), method = Array(RequestMethod.GET))
  def test() = {
    val p = Product (
        "Termometer"
      , "Lorem impsum djfh djshf sdsdf hdskf skdf sdf sdfkjhsd kh ksjdhf sdfjhsdf sdfjkhsdf bnxbvkrhkert eurtz b "
      , "PDI-4711-" + LocalDateTime.now().getNano
      , true
      , Price(12.77F)
      , Image("path")

    )
    productRepository.save(p);

    val c = Comment (
        p.productId
    , "Comment 123 kdsjhfkdh 2."
    , 5
    , "DummyUser"
    , LocalDateTime.now()
    )

    commentRepository.save(c)
  }


}
