package models

import sorm.Persisted

/**
 * Created by developer on 22/10/2015.
 */
case class UpdateProductItemForm (quantity: Double, id: Long, productId: Long, code1: String, description: String, unitCost: Double ) {

}

object UpdateProductItemForm {

  def fromPersistedProductItem(productItem: ProductItem with Persisted) = {
   UpdateProductItemForm(
      quantity = productItem.quantity,
      id = productItem.id,
      productId = productItem.item.asInstanceOf[Persisted].id,
      code1 = productItem.item.code1,
      description = productItem.item.description,
      unitCost = productItem.item.specificTotalCost
   )
  }

}
