package model

import play.api.libs.json.Json

/**
 * Created by developer on 04/10/2015.
 */
case class Product (code1:String,
                    code2:String,
                    description:String,
                    unitType:String,
                    productType:String,
                    taxesFactor:Double,
                    comments:String,
                    specificWorkmanHours:Double,
                    partsWorkmanHours:Double,
                    totalWorkmanHours: Double,
                    totalWorkmanHoursCost:Double,
                    specificMaterialCost:Double,
                    partsMaterialCost:Double,
                    totalMaterialCost:Double,
                    isDirty:Boolean
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

object Product {

  implicit val productFormat = Json.format[Product]

}
