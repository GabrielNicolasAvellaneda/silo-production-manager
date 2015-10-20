package models

import play.api.libs.json.{Writes, OWrites, Json}

/**
 * Created by developer on 04/10/2015.
 */

case class Product ( code1: String = "",
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

trait LowPriorityWriteInstances {
  implicit val productKindWrites = Json.writes[ProductKind]
  implicit val productUnitWrites = Json.writes[ProductUnit]
  implicit val productWrites = Json.writes[Product]

  implicit object persitedWrites extends OWrites[sorm.Persisted] {
    def writes(persisted: sorm.Persisted) = Json.obj("id" -> persisted.id)
  }

}

object Product extends LowPriorityWriteInstances {
  def save(product: Product) = Db.save[Product](product)

  implicit val productPersistedWrites = new Writes[Product with sorm.Persisted] {
    def writes(o: Product with sorm.Persisted) =
      productWrites.writes(o) ++ implicitly[OWrites[sorm.Persisted]].writes(o)
  }

  def all = Db.query[Product]

  def getById(id: Int) = Db.query[Product].whereEqual("id", id).fetchOne()

  def rawMaterialsQuery = {
    val rawMaterialKind = Db.query[ProductKind].whereEqual("id", 1).fetchOne()
    Db.query[Product].whereEqual("kind", rawMaterialKind)
  }

  def getTree(product:Product):ProductTree = {

    val tree = ProductTree(product, Seq())
    val productItems = Db.query[ProductItem].whereEqual("parent", product).fetch()
    productItems foreach { x =>
      tree.children = tree.children :+ getTree(x.item)
    }

    tree
  }


}
