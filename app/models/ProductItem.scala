package models

import play.api.libs.json.Json

/**
 * Created by developer on 18/10/2015.
 */
case class ProductItem (
  parent: Product,
  quantity: Double,
  item: Product )
{

}

object ProductItem {

//  implicit val productItemFormat = Json.format[ProductItem]

}
