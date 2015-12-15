package models

import play.api.libs.json.Json
import sorm.Persisted
import sorm.Persisted
import sorm.persisted.Persisted

/**
 * Created by developer on 22/10/2015.
 */
case class UpdateProductForm
(
  id: Long,
  code1: String,
  code2: Option[String],
  description: String,
  unit: Long,
  kind: Long,
  specificCost: Double,
  specificWorkmanHours: Double,
  items: Seq[UpdateProductItemForm]
  )
{

}

object UpdateProductForm {
  implicit val updateProductItemFormat = Json.format[UpdateProductItemForm]
  implicit val updateProductFormFormat = Json.format[UpdateProductForm]

  def getProductWithUpdates(form:UpdateProductForm) = {
    val id = form.id
    val unit = ProductUnit.getById(form.unit)
    val kind = ProductKind.getById(form.kind)
    val product = Product.getById(id).get
    val updateProduct = product.copy(specificCost = form.specificCost, specificWorkmanHours = form.specificWorkmanHours, description = form.description, kind = kind, unit = unit)
    updateProduct
  }

  def fromPersistedProduct(product:Product with Persisted, items: Seq[ProductItem with Persisted]) = {
    UpdateProductForm(
      id = product.id,
      code1 = product.code1,
      code2 = product.code2,
      description = product.description,
      unit = product.unit.get.asInstanceOf[Persisted].id,
      kind = product.kind.get.asInstanceOf[Persisted].id,
      specificCost = product.specificCost,
      specificWorkmanHours = product.specificWorkmanHours,
      items = items.map(UpdateProductItemForm.fromPersistedProductItem)
    )
  }


}
