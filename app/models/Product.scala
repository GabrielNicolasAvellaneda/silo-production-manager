package models

import play.api.libs.json.{Writes, OWrites, Json}
import sorm.Dsl._
import sorm.Persisted

/**
 * Created by developer on 04/10/2015.
 */

case class Product ( code1: String = "",
                    code2: Option[String] = None,
                    description: String = "",
                    unit: Option[ProductUnit],
                    kind: Option[ProductKind],
                    specificCost: Double = 0,
                    specificWorkmanHours: Double = 0,
                    calculatedCost:Double = 0
                    )
{
  def price = calculatedCost * 1.41

  def specificWorkmanHoursCost = specificWorkmanHours * 15

  def specificTotalCost = specificCost + specificWorkmanHoursCost
}

trait LowPriorityWriteInstances {
  implicit val productKindWrites = Json.writes[ProductKind]
  implicit val productUnitWrites = Json.writes[ProductUnit]
  implicit val productWrites = Json.writes[Product]

  implicit object persitedWrites extends OWrites[sorm.Persisted] {
    def writes(persisted: sorm.Persisted) = Json.obj("id" -> persisted.id)
  }

}

trait ProductWriteInstances extends LowPriorityWriteInstances {
  implicit val productPersistedWrites = new Writes[Product with sorm.Persisted] {
    def writes(o: Product with sorm.Persisted) =
      productWrites.writes(o) ++ implicitly[OWrites[sorm.Persisted]].writes(o) ++ Json.obj("price" -> o.price)
  }
}

object Product extends ProductWriteInstances {

  val queryResultLimit = 40

  def save(product: Product) = Db.save[Product](product)

  def update(product: Product, items: Seq[ProductItem]) = {
    var updated: Product with Persisted = null
    Db.transaction {
      updated = Db.save[Product](product)
      ProductItem.updateItemsForParentId(items, product.asInstanceOf[Persisted].id)
      updateProductCost(product)
    }
    updated
  }


  def all = Db.query[Product]

  def getById(id: Long) = Db.query[Product].whereEqual("id", id).fetchOne()

  // TODO: Accept a map with field, value, operator.
  def search (text: Option[String], kind: Option[Int] = None) = {

    Db.query[Product]
      .where("code1" like text.get)
      .order("code1")
      .limit(queryResultLimit)
      .fetch()
  }

  def rawMaterialsQuery = {
    val rawMaterialKind = Db.query[ProductKind].whereEqual("id", 1).fetchOne()
    Db.query[Product].whereEqual("kind", rawMaterialKind).limit(queryResultLimit)
  }

  def getTree(product:Product):ProductTree = {

    val tree = ProductTree(product, Seq())
    val productItems = ProductItem.getItemsByParent(product)
    productItems foreach { x =>
      tree.children = tree.children :+ getTree(x.item)
    }
    tree
  }

  def calculateItemsCostForProduct(parent:Product) = {
    val items = ProductItem.getItemsByParent(parent)
    items.foldLeft(0.0)((a, b) => a + b.item.calculatedCost * b.quantity)
  }

  def calculateProductCost(product:Product): Double = {
    calculateItemsCostForProduct(product) + product.specificTotalCost
 }

  def updateProductCost(product:Product) = {
    val calculatedCost = calculateProductCost(product)
    val updateProduct = product.copy(calculatedCost = calculatedCost)
    Db.save[Product](updateProduct)
  }

  def updateCostsForItem(item:Product):Unit = {
    val parents = ProductItem.getParentsForItem(item)
    parents foreach {
      x =>
        val updatedProduct = updateProductCost(x)
        updateCostsForItem(updatedProduct)
    }
  }

}
