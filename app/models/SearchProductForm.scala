package models

import play.api.libs.json._

/**
 * Created by developer on 20/10/2015.
 */
case class SearchProductForm (text: Option[String], kind: Option[Int]) {

}

object SearchProductForm {
  implicit val searchProductFormFormat = Json.format[SearchProductForm]
}
