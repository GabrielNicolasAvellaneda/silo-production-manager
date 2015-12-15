package models

import sorm.Persisted

/**
 * Created by developer on 22/10/2015.
 */
case class UpdateProductItemForm (quantity: Double, productId: Long, description: String, unitCost: Double ) {

}

object UpdateProductItemForm {

  def fromPersistedProductItem(productItem: ProductItem with Persisted) = {
   UpdateProductItemForm(
      quantity = productItem.quantity,
      productId = productItem.parent.asInstanceOf[Persisted].id,
      description = productItem.item.description,
      unitCost = productItem.item.specificTotalCost
   )
  }

}
