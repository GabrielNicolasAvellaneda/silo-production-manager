package controllers

import javax.inject.{Inject, Singleton}
import akka.actor.ActorSystem
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json._
import play.api.mvc.{Action, BodyParsers, Controller}

/**
 * Created by developer on 04/10/2015.
 */
@Singleton
class ProductController @Inject()(val messagesApi: MessagesApi, system: ActorSystem) extends Controller with I18nSupport  {

  val costsUpdaterActor = system.actorOf(CostsUpdaterActor.props, "costsUpdaterActor")

  val searchProductForm = Form(
    single("query" -> text)
  )

  def getProduct(id: Int) = Action {
    val product = Product.getById(id).get
    val items = ProductItem.getItemsByParent(product)
    val form = UpdateProductForm.fromPersistedProduct(product, items)
    Ok(Json.toJson(form))
  }

  def getProductItems(id: Int) = Action {
   val items = ProductItem.getItemsByParentId(id)
    Ok(Json.toJson(items))
  }

  def productTree(id: Int) = Action {
    val product = Product.getById(id)
    val tree = Product.getTree(product.get)
    Ok(Json.toJson(tree))
  }

  def listProducts(all: Boolean) = Action {
    if (all) {
      val products = Product.all.fetch()
      Ok(Json.toJson(products take 10))
    } else {
      val rawMaterials = Product.rawMaterialsQuery.fetch()
      val json = Json.toJson(rawMaterials take 10)
      Ok(json)
    }
  }

  def productSearch() = Action(BodyParsers.parse.json) { request =>
    val query = request.body.validate[SearchProductForm]
    query.fold (
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
      },
      form => {
        val result = Product.search(form.text)
        Ok(Json.toJson(result))
      }
    )
 }

  def listProductKinds() = Action {
    val productKinds = Db.query[ProductKind].fetch()
    Ok(Json.toJson(productKinds))
  }

  def listProductUnits() = Action {
    val productUnits = Db.query[ProductUnit].fetch()
    Ok(Json.toJson(productUnits))
  }

  def newProduct() = Action(BodyParsers.parse.json) { request =>
    val productNewResult = request.body.validate[NewProductForm]
    productNewResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
      },
      form => {
        val unit = ProductUnit.getById(form.unit)
        val kind = ProductKind.getById(form.kind)
        val product = Product(form.code1, form.code2, form.description, unit, kind)
        val result = Product.save(product)
        Ok(Json.toJson(result))
      }
    )
  }

  def updateProduct() = Action(BodyParsers.parse.json) { request =>
    val productUpdateResult = request.body.validate[UpdateProductForm]
    productUpdateResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toFlatJson(errors)))
      },
      form => {
        val id = form.id
        val unit = ProductUnit.getById(form.unit)
        val kind = ProductKind.getById(form.kind)
        val product = Product.getById(id).get
        val updateProduct = product.copy()
        val items: Seq[ProductItem] = null
        val updated = Product.update(updateProduct, items)
        costsUpdaterActor ! CostsUpdaterActor.UpdateCostsMessage(updated)
        Ok(Json.toJson(updated))
      }
    )
  }

}
