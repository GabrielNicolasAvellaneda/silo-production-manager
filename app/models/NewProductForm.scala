package models

import play.api.libs.json._

/**
 * Created by developer on 06/10/2015.
 */
case class NewProductForm (
                    code1: String,
                    code2: Option[String],
                    description: String,
                    unit: Int,
                    kind: Int,
                    specificCost: Int,
                    specificWorkmanHours: Int
                   ) {
}

object NewProductForm {

 implicit val newProductFormFormat = Json.format[NewProductForm]
}
