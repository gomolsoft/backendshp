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

case class ApiProduct(
                       @BeanProperty var name: String
                     , @BeanProperty var description: String
                     , @BeanProperty var shortdescription: String
                     , @BeanProperty var id: String
                     , @BeanProperty var productId: String
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
    case p: Product => ApiProduct(
                      p.name
                    , p.description
                    , p.shortdescription
                    , p.id
                    , p.productId
                    , build(p.image)
                    , p.inStock
                    , build(p.price)
                    , build(commentRepository.findByProductId(p.productId)))
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
  @RequestMapping(value = Array("/{id}"), produces = Array("application/json"), method = Array(RequestMethod.POST))
  def product(@PathVariable("id") id: String): ApiResultEntity = {
    val product = productRepository.findOne(id)
    build(product)
  }

  //@PreAuthorize("hasRole('ROLE_DOMAIN_USER')")
  @RequestMapping(value = Array("/test"), produces = Array("application/json"), method = Array(RequestMethod.GET))
  def test() = {
    val p1 = Product (
        "Termometer"
      , "Lorem impsum djfh djshf sdsdf hdskf skdf sdf sdfkjhsd kh ksjdhf sdfjhsdf sdfjkhsdf bnxbvkrhkert eurtz b "
      ,"genaue Raumtemperatur-Messung"
      , "PDI-4711-" + Calendar.getInstance().getTime.getTime
      , true
      , Price(12.77F)
      , Image("http://placehold.it/960x720")

    )
    productRepository.save(p1)

    val c1_1 = Comment (
        p1.productId
    , "Comment 123 kdsjhfkdh 2."
    , 5
    , "DummyUser"
    , Calendar.getInstance().getTime
    )

    commentRepository.save(c1_1)


    val p2a = Product (
      "Thermostat Gruppe"
      , "Lorem impsum djfh djshf sdsdf hdskf skdf sdf sdfkjhsd kh ksjdhf sdfjhsdf sdfjkhsdf bnxbvkrhkert eurtz b "
      ,"für die optimale Temperaturregelung im 3er Pack"
      , "PDI-4712-a-" + Calendar.getInstance().getTime.getTime
      , true
      , Price(43.99F)
      , Image("http://placehold.it/930x700")

    )
    productRepository.save(p2a)

    val p2 = Product (
      "Thermostat"
      , "Lorem impsum djfh djshf sdsdf hdskf skdf sdf sdfkjhsd kh ksjdhf sdfjhsdf sdfjkhsdf bnxbvkrhkert eurtz b "
      ,"für die optimale Temperaturregelung"
      , "PDI-4712-" + Calendar.getInstance().getTime.getTime
      , true
      , Price(36.77F)
      , Image("http://placehold.it/1200x900")

    )
    productRepository.save(p2)

    val c2_1 = Comment (
      p2.productId
      , "khdsf ksdhf dhf."
      , 3
      , "MyDummyUser"
      , Calendar.getInstance().getTime
    )
    val c2_2 = Comment (
      p2.productId
      , "Comment 123 kdsjhfkdh 2."
      , 4
      , "NoDummyUser"
      , Calendar.getInstance().getTime
    )

    commentRepository.save(c2_1)
    commentRepository.save(c2_2)
  }



}
