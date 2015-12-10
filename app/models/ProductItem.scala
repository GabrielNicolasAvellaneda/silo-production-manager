package models

import play.api.libs.json.{OWrites, Writes, Json}

/**
 * Created by developer on 18/10/2015.
 */
case class ProductItem (
  parent: Product,
  quantity: Double,
  item: Product ) // TODO: Change to product
{
  def calculatedCost = quantity * item.calculatedCost
}

trait LowPriorityProductItemWriteInstances extends ProductWriteInstances {
  implicit val productItemWrites = Json.writes[ProductItem]
}

object ProductItem extends LowPriorityProductItemWriteInstances {

  implicit val productItemPersistsedWrites = new Writes[ProductItem with sorm.Persisted] {
    def writes(o: ProductItem with sorm.Persisted) =
      productItemWrites.writes(o) ++ implicitly[OWrites[sorm.Persisted]].writes(o) ++ Json.obj("id" -> o.id)
  }

  def getItemsByParentId(parentId: Long) = {
    Db.query[ProductItem].whereEqual("parent.id", parentId).fetch()
  }

  def getItemsByParent(parent:Product) = {
    Db.query[ProductItem].whereEqual("parent", parent).fetch()
  }

  def getParentsForItem(item:Product) = {
    Db.query[ProductItem].whereEqual("item", item).fetch().map(x => x.parent)
  }
}
