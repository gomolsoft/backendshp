package de.moso.controller

import java.util

import de.moso.entity.{Image, Price, Product}
import de.moso.repository.ProductRepository
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
                     , @BeanProperty var price: Float )



@RestController
@RequestMapping(Array("/product"))
class ProductController {
  @Autowired var productRepository: ProductRepository = _

  //@PreAuthorize("hasRole('ROLE_DOMAIN_USER')")
  @RequestMapping(value = Array("/products"), produces = Array("application/json"), method = Array(RequestMethod.GET))
  def allDevices(): util.List[ApiProduct] = {
    val build = (pt: Product) => {

      val i = Some(pt.image)
      val p = Some(pt.price)
      ApiProduct(pt.name,pt.description,i.getOrElse(Image("")).path,pt.inStock,p.getOrElse(Price(-1)).price)
    }

    val products = productRepository.findAll()
    products
      .map( (p:Product) => build(p))
  }

  //@PreAuthorize("hasRole('ROLE_DOMAIN_USER')")
  @RequestMapping(value = Array("/test"), produces = Array("application/json"), method = Array(RequestMethod.GET))
  def test() = {
    val p = Product(
        "Termometer"
      , "Lorem impsum djfh djshf sdsdf hdskf skdf sdf sdfkjhsd kh ksjdhf sdfjhsdf sdfjkhsdf bnxbvkrhkert eurtz b "
      , true
      , Price(12.77F)
      , Image("path")
    )
    productRepository.save(p);
  }


}
