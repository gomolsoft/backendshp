package de.moso.controller

import java.util
import java.util.{Calendar, Collections, Date}

import de.moso.config.ShoppingCartService
import de.moso.entity._
import de.moso.repository.{ProductCommentRepository, ProductDiscountRepository, ProductRepository}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._
import org.springframework.web.context.request.RequestContextHolder

import scala.beans.BeanProperty

/**
 * Created by sandro on 27.05.15.
 */

trait ApiResultEntity extends ShopEntity

case class ApiProduct(
                       @BeanProperty var productId: String

                     , @BeanProperty var name: String
                     , @BeanProperty var shortDescription: String
                     , @BeanProperty var id: String
                     , @BeanProperty var img: String
                     , @BeanProperty var inStock: Boolean
                     , @BeanProperty var discountPrice: Float
                     , @BeanProperty var price: Float
                     , @BeanProperty var discount: Int
                     , @BeanProperty var ratingAverage: Float
                       ) extends ApiResultEntity


case class ApiProductDetail (
                              @BeanProperty var apiProduct: ApiProduct

                            , @BeanProperty var detailLongDescription: String
                            , @BeanProperty var tecnicalDetailsDescription: String
                            , @BeanProperty var comments: util.List[ApiProductComment]
                              ) extends  ApiResultEntity


case class ApiProductComment(
                         @BeanProperty var comment: String
                       , @BeanProperty var user: String
                       , @BeanProperty var rating: Int
                       ) extends ApiResultEntity


case class ApiCartItem (
                        @BeanProperty var apiProduct: ApiProduct
                      , @BeanProperty var quantity: Int
                        )

case class ApiCart (
                     @BeanProperty var total: Float
                   , @BeanProperty var comments: util.List[ApiCartItem]

                    )

@RestController
@RequestMapping(Array("/product"))
class ProductController {

  import collection.JavaConversions._

  @Autowired var productRepository: ProductRepository = _
  @Autowired var commentRepository: ProductCommentRepository = _
  @Autowired var discountRepository: ProductDiscountRepository = _

  @Autowired var shopingCartService: ShoppingCartService = _

  def today = Calendar.getInstance().getTime
  val isDateInNow = (dateToCheckFrom: Date, dateToCheckTo: Date) => today.after(dateToCheckFrom) && today.before(dateToCheckTo)

  def buildImagePath(i:Image): String = i match {
    case Image(path) => path
    case _ => "-"
  }

  def buildPrice(p:Price): Float = p match {
    case Price(price) => price
    case _ => Float.NaN
  }

  def buildProductComment(c: Comment): ApiProductComment = c match {
    case c: Comment => ApiProductComment(c.comment, c.username, c.rating)
  }

  def buildProductComments(e: AnyRef): util.List[ApiProductComment] = e match {
    case e: Product => buildProductComments(commentRepository.findByProductId(e.productId))
    case e: java.util.List[Comment] => e.map( (c:Comment) => buildProductComment(c) )
    case _ => Collections.emptyList[ApiProductComment]
  }


  def buildRatingAverage(c:java.util.List[Comment]): Float = c match {
    case lc: java.util.List[Comment] => if (lc.size>0) ( (lc.map (_.rating) sum).toFloat  / lc.size().toFloat) else Float.NaN
    case _ => Float.NaN
  }

  def buildDiscountPrice(d:java.util.List[Discount]): Float = d match {
    case lc: java.util.List[Discount] => lc.find(d => isDateInNow(d.validFrom, d.validTo) ).getOrElse(Discount.emtyDiscount).discountPrice.price
    case _ => Float.NaN
  }

  def buildDiscountPercent(d:java.util.List[Discount]): Int = d match {
    case lc: java.util.List[Discount] => lc.find(d => isDateInNow(d.validFrom, d.validTo)).getOrElse(Discount.emtyDiscount).discountPercent
    case _ => Int.MinValue
  }

  def buildApiProductDetail[A >: ShopEntity](u: ShopEntity): ApiResultEntity = u match {
    case p: Product => ApiProductDetail(
                apiProduct = buildApiProduct(p)
              , detailLongDescription = p.productDescription.longDescription
              , tecnicalDetailsDescription = p.productDescription.technicalDescription
              , comments =  buildProductComments(p)
            )
  }

  def buildApiProduct[A >: ShopEntity](u: ShopEntity): ApiProduct = u match {
    case (p: Product) => ApiProduct(
        id               = p.id
      , productId        = p.productId
      , name             = p.name
      , shortDescription = p.productDescription.shortDescription
      , img              = buildImagePath(p.image)
      , inStock          = p.inStock
      , discountPrice    = buildDiscountPrice(discountRepository.findByProductId(p.productId) )
      , price            = buildPrice(p.price)
      , discount         = buildDiscountPercent(discountRepository.findByProductId(p.productId) )
      , ratingAverage    = buildRatingAverage(commentRepository.findByProductId (p.productId) )
    )
  }

  //@PreAuthorize("hasRole('ROLE_DOMAIN_USER')")
  @RequestMapping(value = Array("/products"), produces = Array("application/json"), method = Array(RequestMethod.GET))
  def allDevices(): java.util.List[ApiResultEntity] = {

    val products = productRepository.findAll()

    import collection.JavaConversions._
    val r = products.map( (p:Product) => buildApiProduct(p) )
    r.toList

  }

  //@PreAuthorize("hasRole('ROLE_DOMAIN_USER')")
  @RequestMapping(value = Array("/{id}"), produces = Array("application/json"), method = Array(RequestMethod.POST))
  def product(@PathVariable("id") id: String): ApiResultEntity = {
    val product = productRepository.findOne(id)
    buildApiProductDetail(product)
  }

  val shopingCartManager = (sid: String, productId: String, quantity:Int) => {
    val apiCart = if (shopingCartService.contains(sid)) shopingCartService.retrieve(sid)

    val shopCartItem = (apiCart, productId, quantity) match {
      case (_, productId: String, quantity: Int) =>
    }

  }

  //@PreAuthorize("hasRole('ROLE_DOMAIN_USER')")
  @RequestMapping(value = Array("/cart"), produces = Array("application/json"), method = Array(RequestMethod.POST))
  def product(@PathVariable("productId") productId: String, @PathVariable("quantity") quantity: Integer): ApiResultEntity = {
    val sid = RequestContextHolder.currentRequestAttributes().getSessionId()

    null

  }


  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  //@PreAuthorize("hasRole('ROLE_DOMAIN_USER')")
  @RequestMapping(value = Array("/test"), produces = Array("application/json"), method = Array(RequestMethod.GET))
  def test() = {
    val p1 = Product (
        name = "Heizkörperthermostat"
      , productDescription = ProductDescription (
              shortDescription="Heizungssteuerung mit Energiesparpotenzial"
            , longDescription = "<p>Heizungssteuerung mit My-Smart-Things SmartHome bedeutet maximales Energiesparpotenzial in Kombination mit gesteigertem Wohngefühl. Stellen Sie sich vor, Sie kommen an einem kalten Wintertag früher als erwartet nach Hause und in Ihren Räumen finden Sie dennoch eine angenehme Temperatur vor.</p><p><strong>Behaglichkeit steigern – Energie sparen</strong><br><span style=\"display:table-cell; width:128px;\" class=\"float--left\"><span class=\"lightbox\"><img title=\"Intelligente Heizungssteuerung per My-Smart-Things SmartHome Heizkörperthermostat\" src=\"/web/cms/mediablob/de/462780/data/8/Heizkoerperthermostat.jpg\" alt=\"Intelligente Heizungssteuerung per My-Smart-Things SmartHome Heizkörperthermostat\" height=\"85\" width=\"128\"><span class=\"overlay\"><a btattached=\"true\" title=\"&nbsp;\" rel=\"shadowbox[462780]\" href=\"/web/cms/mediablob/de/462780/blowupData/8/Heizkoerperthermostat.jpg\"><img title=\"größere Ansicht\" src=\"/web/cms/contentblob/162002/data/501/3-picture.gif\" alt=\"\"></a></span></span></span>Mit dem My-Smart-Things SmartHome Heizkörperthermostat ist dieser Komfort im Rahmen Ihrer Heizungssteuerung problemlos möglich. Jetzt können Sie die Raumtemperatur nicht nur Zuhause, sondern auch bequem von unteMy-Smart-Thingsgs* via Smartphone* und Internet anpassen. Über eingestellte Profile weiß Ihre Heizungssteuerung, wann geheizt werden muss und wann gespart werden kann. Mehr Komfort und gleichzeitig Energie sparen – kein Problem mit dem My-Smart-Things SmartHome Heizkörperthermostat.</p><p><strong>Wie funktioniert das Heizkörperthermostat von My-Smart-Things SmartHome?</strong><br>Das My-Smart-Things SmartHome Heizkörperthermostat ist einfach installierbar und wird an Stelle des mechanischen Thermostatkopfes montiert. Es besteht aus einem Temperaturfühler, einer Ansteuerelektronik und einem Stellantrieb, die in einem Gehäuse zusammengefasst sind: Ihre Heizungssteuerung fügt sich somit dezent in das&nbsp; Gesamtbild Ihres Wohndesigns ein. Der Stellantrieb bewegt das Heizkörperventil, um den Zustrom der Wärme in den Heizkörper zu steuern. Der Temperaturfühler misst die Raumtemperatur. Er vergleicht die gemessene Temperatur mit einer von der <a btattached=\"true\" title=\"Hausautomatisierung&nbsp;mit My-Smart-Things SmartHome über eine Zentrale\" target=\"_self\" onclick=\"\" href=\"/web/cms/de/459226/smarthome/informieren/geraete/zentrale/\">My-Smart-Things SmartHome Zentrale</a> oder vom Stellrad vorgegebenen Soll-Temperatur und regelt bei Bedarf nach. So erhalten Sie eine Heizungssteuerung mit maximalem Komfort.</p><p><strong>Heizungssteuerung mit dem Heizkörperthermostat&nbsp;von My-Smart-Things SmartHome</strong><br>Mit der modernen Technologie von My-Smart-Things SmartHome können Sie Ihre Heizung jederzeit steuern: Sie können das Heizkörperthermostat manuell, per <a btattached=\"true\" title=\"Fernbedienung&nbsp;von My-Smart-Things SmartHome\" target=\"_self\" onclick=\"\" href=\"/web/cms/de/459230/smarthome/informieren/geraete/fernbedienung/\">My-Smart-Things SmartHome Fernbedienung</a>, per <a btattached=\"true\" title=\"Wandsender&nbsp;– Intelligenter Funkschalter von My-Smart-Things SmartHome \" target=\"_self\" onclick=\"\" href=\"/web/cms/de/459238/smarthome/informieren/geraete/wandsender/\">My-Smart-Things SmartHome Wandsender</a>, per Smartphone* oder über das Internet regeln. Mit dem My-Smart-Things SmartHome Heizkörperthermostat wird Ihre Heizungssteuerung zum Kinderspiel.</p><p><strong>Download:</strong><br><a btattached=\"true\" title=\"\" target=\"_blank\" onmousedown=\"ET_Event.download('/de/2283088/data/459234/2/smarthome/informieren/geraete/heizkoerperthermostat/Kompatibilitaetsliste-des-My-Smart-Things-SmartHome-Heizkoerperthermostats-zu-handelsueblichen-Heizkoerperventilen.pdf', '')\" onclick=\"\" href=\"/web/cms/mediablob/de/2283088/data/459234/2/smarthome/informieren/geraete/heizkoerperthermostat/Kompatibilitaetsliste-des-My-Smart-Things-SmartHome-Heizkoerperthermostats-zu-handelsueblichen-Heizkoerperventilen.pdf\" class=\"download pdf\">Kompatibilitätsliste des My-Smart-Things SmartHome Heizkörperthermostats zu handelsüblichen Heizkörperventilen&nbsp;(<acronym title=\"Portable Document Format\" lang=\"en\">PDF</acronym>&nbsp;|&nbsp;153&nbsp;<acronym>KB</acronym>)</a></p><p><strong>Ihre Vorteile:</strong></p><ul><li>Intelligente Heizungssteuerung</li><li>Mehr Komfort und gleichzeitig Energie sparen</li><li><p>Heizungssteuerung von My-Smart-Thingsgs per Smartphone und Internet</p></li></ul>"
            , technicalDescription = "technind lfkjg lskdfjg  dsflkgj ilösdjfglö sldgj lkdsjf g")
      , productId =  "PDI-4711-" + Calendar.getInstance().getTime.getTime
      , inStock =  true
      , price = Price(12.99F)
      , image = Image("http://placehold.it/960x720")

    )
    productRepository.save(p1)

    val c1_1 = Comment (
         productId = p1.productId
      ,  comment = "Comment 123 kdsjhfkdh 2."
      ,  rating =  5
      ,  username =  "DummyUser"
      ,  date = Calendar.getInstance().getTime
    )

    commentRepository.save(c1_1)


    val p2a = Product (
      name = "SmartHome Zwischenstecker "
      , productDescription = ProductDescription (
          shortDescription="Automatische Rautemperaturregelung"
        , longDescription = "Nachts mehr Sicherheit (Lampensteuerung)<br/> Einbruchschutz: Anwesenheitssimulation mit Licht <p><br> Der dimmbare SmartHome Zwischenstecker integriert als Funkdimmer Stimmungs- und Effektbeleuchtungen in Ihre SmartHome Lösung. Ganz nach Ihrem Bedarf sorgt der Funkdimmer so automatisch für die richtige Beleuchtung.<br><br> <strong>Der dimmbare SmartHome Zwischenstecker: Sanftes Licht per Funkdimmer<br><br> </strong>Wie hell Ihre Lampen in Haus und Wohnung leuchten, steuern Sie ganz einfach über Ihre My-Smart-Things SmartHome Zentrale. Zum Beispiel automatisch gedimmtes Licht, wenn Sie Ihren Fernseher einschalten. Oder sanftes Licht in Flur oder Treppenhaus, wenn die Kinder schlafen.</p>"
        , technicalDescription = "<table class=\"boxattributes\" summary=\"Produktdetails\">\n\n<tbody><tr>\n<th colspan=\"2\" align=\"left\">Produktdetails</th>\n</tr>\n\n<tr>\n<td class=\"name\" align=\"right\">Gerätebezeichnung:\n</td>\n<td class=\"value\">PSD</td>\n</tr>\n\n<tr>\n<td class=\"name\" align=\"right\">Versorgungsspannung:\n</td>\n<td class=\"value\">230 V/ 50 Hz</td>\n</tr>\n\n<tr>\n<td class=\"name\" align=\"right\">Maximale Sendeleistung:\n</td>\n<td class=\"value\">10 mW</td>\n</tr>\n\n<tr>\n<td class=\"name\" align=\"right\">Standby-Verbrauch:\n</td>\n<td class=\"value\">1 W</td>\n</tr>\n\n<tr>\n<td class=\"name\" align=\"right\">Dimmverfahren:\n</td>\n<td class=\"value\">Phasenabschnitt</td>\n</tr>\n\n<tr>\n<td class=\"name\" align=\"right\">Funkfrequenz:\n</td>\n<td class=\"value\">868,3 MHz</td>\n</tr>\n\n<tr>\n<td class=\"name\" align=\"right\">Abmessungen:\n</td>\n<td class=\"value\">60x130x77 mm (BxHxT)</td>\n</tr>\n\n<tr>\n<td class=\"name\" align=\"right\">Gewicht:\n</td>\n<td class=\"value\">180 g</td>\n</tr>\n\n\n\n\n\n</tbody></table>")
      , productId =  "PDI-4712-" + Calendar.getInstance().getTime.getTime
      , inStock =  true
      , price = Price(39.99F)
      , image = Image("http://placehold.it/930x700")

    )
    productRepository.save(p2a)

    val di2a = Discount(p2a.productId, Price(29.99F), 25, new Date(115,1,2), new Date(115,9,31) )
    discountRepository.save(di2a)

    val p2 = Product (
      name = "Home HD Kamera"
      , productDescription = ProductDescription (
          shortDescription="Kamera Raumüberwachung"
        , longDescription = "Eine Weitwinkelkamera, die live auf dein Smarthphone streamt UND einen Lautsprecher besitzt, so dass du darüber sprechen kannst UND die die Luftqualität misst UND auch noch richtig gut dabei aussieht. Das isses!<br/>Es gibt unterschiedliche Gründe für eine Echtzeit-Übertragen von zu Hause auf dein Smartphone - uns fallen mindestens 1984 ein! Man macht sich Sorgen, dass eingebrochen werden könnte. Man braucht ein Babyphone. Oder man will mit seiner Familie zu Abend essen, obwohl man auf Geschäftsreise am anderen Ende der Welt ist.<br/><br/>Egal was deine Gründe sind, Withings Home ist eine echte Premiumlösung. Durch die formschöne Ausführung in Holz wirkt sie nicht wie ein gewöhnliche „Überwachungskamera“. Die Übertragung erfolgt live rund um die Uhr über die Gratisapp Withings Home auf dein mobiles Endgerät. Dank dem eingebauten Lautsprecher und Mikrofon möchten wir sie eigentlich lieber als Dialogkamera bezeichnen: Egal wo du dich auf der Welt gerade befindest - du kannst über die Kamera den Kindern gute Nacht sagen oder monoton schnarren „Eindringling identifiziert - Schlafgasfreisetzung in 5 ... 4... 3 ... 2 ... 1 zsssssssssssssssssssss“, wenn du einen Einbrecher auf dem Display entdeckst.<br/><br/>Und dass die Kamera über ein 12x Zoom, 135°-Weitwinkelobjektiv, automatische Nachtsicht, Bewegungsdetektor, die Möglichkeit, sämtliche Geschehnisse der letzten zwölf Stunden zu überwachen und einen Raumluftmesser verfügt, hatten wir bis jetzt noch gar nicht erwähnt. Jepp – du kannst die ganze Zeit über die Luftqualität bei dir zu Hause in Echtzeit kontrollieren, damit du genau weißt, wann es Zeit ist ein Fenster zu öffnen, damit eventuell noch vorhandenes Schlafgas entweichen kann."
        , technicalDescription = "<h2>Produktmerkmale Withings Home Überwachungskamera:</h2><ul>\n<li>Formschöne HD-Kamera für die Videoübertragung auf dein Mobilgerät in Echtzeit!</li>\n<li>Perfekt als Babyphone, bei Einbruch oder um mit der Familie zusammen zu sein</li>\n<li>Misst auch die Raumluftqualität bei dir zu Hause</li>\n<li>Maße: 9 cm hoch 7,5 cm im Durchmesser</li>\n<li>Gewicht: 0,45 kg</li>\n<li>Wenn die Sensoren etwas erfasst haben, wird eine Mitteilung an das Mobilgerät gesendet</li>\n<li>Zeitraffer-Modus: Damit du im Schnelldurchlauf sehen kannst, was in den letzten 12 Stunden passiert ist</li>\n<li>Funktion zur Erstellung eines automatischen „Tagebuchs“</li>\n<li>Verbindung über dein WLAN</li>\n<li>Betrieb mit dem mitgelieferten Stromadapter - kann auch per Micro USB Kabel betrieben werden (nicht im Lieferumfang enthalten)</li>\n<li>Bei schlechter Luftqualität leuchtet das Unterteil der Kamera rot</li>\n\n<h2>Kamera:</h2>\n<li>5 Megapixel CMOS-Sensor</li>\n<li>Videokodierung bis 1080p, 30 Bilder pro Sekunde</li>\n<li>135°-Winkel (kein Fischauge)</li>\n<li>Nachtsicht mit mechanischem IR-Filter</li>\n\n<h2>Sensoren:</h2>\n<li>Luftqualität (Messung von leicht flüchtigen organischen Verunreinigungen)</li>\n<li>3-Achsen-Beschleunigungssensor\n</li><li>Umgebungslicht-Sensor</li>\n\n<p>Kompatibel mit iPhone (4s und höher), iPad (Generation 3 und höher), iPod Touch (Generation 4 und höher)\nBenötigt iOS 7 oder später</p>\n\n                      </ul>")
      , productId =  "PDI-4712-" + Calendar.getInstance().getTime.getTime
      , inStock =  true
      , price = Price(49.99F)
      , image = Image("http://placehold.it/1200x900")

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
