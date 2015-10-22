package models

import play.api.libs.json.Json

/**
 * Created by developer on 22/10/2015.
 */
case class UpdateProductForm
(
  id: Int,
  code1: String,
  code2: Option[String],
  description: String,
  unit: Int,
  kind: Int,
  specificCost: Double,
  specificWorkmanHours: Int,
  items: Seq[UpdateProductItemForm]
  )
{

}

object UpdateProductForm {
  implicit val updateProductItemFormat = Json.format[UpdateProductItemForm]
  implicit val updateProductFormFormat = Json.format[UpdateProductForm]
}
