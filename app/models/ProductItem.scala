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

  def calculatedCost = quantity * item.calculatedCost

}

object ProductItem {

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
