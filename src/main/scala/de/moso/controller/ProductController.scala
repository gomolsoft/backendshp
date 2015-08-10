package de.moso.controller

import java.util
import java.util.{Calendar, Collections}

import de.moso.entity._
import de.moso.repository.{ProductCommentRepository, ProductRepository}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._

import scala.beans.BeanProperty

/**
 * Created by sandro on 27.05.15.
 */

trait ApiResultEntity extends ShopEntity

case class ApiProduct( @BeanProperty var name: String
                     , @BeanProperty var description: String
                     , @BeanProperty var img: String
                     , @BeanProperty var inStock: Boolean
                     , @BeanProperty var price: Float
                     , @BeanProperty var comments: util.List[ApiProductComment]
                       ) extends ApiResultEntity

case class ApiProductComment(
                         @BeanProperty var comment: String
                       , @BeanProperty var user: String
                       , @BeanProperty var rating: Int
                       ) extends ApiResultEntity


@RestController
@RequestMapping(Array("/product"))
class ProductController {
  @Autowired var productRepository: ProductRepository = _
  @Autowired var commentRepository: ProductCommentRepository = _

  def build(i:Image): String = i match {
    case Image(path) => path
    case _ => ""
  }

  def build(p:Price): Float = p match {
    case Price(price) => price
    case _ => -1.0F
  }

  def build(c: Comment): ApiProductComment = c match {
    case c: Comment => ApiProductComment(c.comment, c.username, c.rating)
  }

  import collection.JavaConversions._
  def build(c:java.util.List[Comment]): util.List[ApiProductComment] = c match {
    case lc: java.util.List[Comment] => lc.map( (c:Comment) => build(c) )
    case _ => Collections.emptyList[ApiProductComment]
  }

  def build[A >: ShopEntity](u: ShopEntity): ApiResultEntity = u match {
    case p: Product => ApiProduct(p.name, p.description, build(p.image), p.inStock, build(p.price), build(commentRepository.findByProductId(p.productId)))
  }

  //@PreAuthorize("hasRole('ROLE_DOMAIN_USER')")
  @RequestMapping(value = Array("/products"), produces = Array("application/json"), method = Array(RequestMethod.GET))
  def allDevices(): java.util.List[ApiResultEntity] = {

    val products = productRepository.findAll()

    import collection.JavaConversions._
    val r = products.map( (p:Product) => build(p) )
    r.toList

  }

  //@PreAuthorize("hasRole('ROLE_DOMAIN_USER')")
  @RequestMapping(value = Array("/test"), produces = Array("application/json"), method = Array(RequestMethod.GET))
  def test() = {
    val p = Product (
        "Termometer"
      , "Lorem impsum djfh djshf sdsdf hdskf skdf sdf sdfkjhsd kh ksjdhf sdfjhsdf sdfjkhsdf bnxbvkrhkert eurtz b "
      , "PDI-4711-" + Calendar.getInstance().getTime.getTime
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
    , Calendar.getInstance().getTime
    )

    commentRepository.save(c)
  }


}
