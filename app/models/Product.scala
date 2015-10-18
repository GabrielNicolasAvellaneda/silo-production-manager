package models

import play.api.libs.json.Json

/**
 * Created by developer on 04/10/2015.
 */

case class Product( code1: String = "",
                    code2: Option[String] = None,
                    description: String = "",
                    unit: Option[ProductUnit],
                    kind: Option[ProductKind]
                    )


/*
case class Product (code1:String = "",
                    code2:String = "",
                    description:String = "",
                    unitType:String = "",
                    productType:String = "",
                    taxesFactor:Double = 1,
                    comments:String = "",
                    specificWorkmanHours:Double = 0,
                    partsWorkmanHours:Double = 0,
                    totalWorkmanHours: Double = 0,
                    totalWorkmanHoursCost:Double = 0,
                    specificMaterialCost:Double = 0,
                    partsMaterialCost:Double = 0,
                    totalMaterialCost:Double = 0,
                    isDirty:Boolean = false
                    )
{

  def totalCost:Double = {
    totalMaterialCost + totalWorkmanHoursCost
  }

  def price:Double = {
    totalCost * 1.41
  }

  def priceWithTaxes:Double = {
    price * taxesFactor
  }
}
*/


object Product {

  implicit val productKindFormat = Json.format[ProductKind]
  implicit val productUnitFormat = Json.format[ProductUnit]
  implicit val productFormat = Json.format[Product]

  def all = Db.query[Product]

  def rawMaterialsQuery = {
    val rawMaterialKind = Db.query[ProductKind].whereEqual("id", 1).fetchOne()
    Db.query[Product].whereEqual("kind", rawMaterialKind)
  }


}
