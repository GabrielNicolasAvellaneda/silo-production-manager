package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import sorm.Persisted

/**
 * Created by developer on 18/10/2015.
 */
case class ProductTree (product:Product, quantity: Double, var children: Seq[ProductTree]) {

}

object ProductTree {

  implicit val productUnitFormat = Json.format[ProductUnit]
  implicit val productKindFormat = Json.format[ProductKind]
  implicit val productWrites = new Writes[Product] {
    def writes(product:Product) = Json.obj(
      "description" -> product.description,
      "code1" -> product.code1,
      "kind" -> product.kind.get.asInstanceOf[Persisted].id
    )

  }

  implicit val productTreeFormat = Json.writes[ProductTree]

}
