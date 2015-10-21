package controllers

import javax.inject.Inject

import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi, Messages, Lang}
import play.api.mvc.{BodyParsers, Controller, Action}
import play.api.libs.json._
import views._

import scala.collection.JavaConverters._

/**
 * Created by developer on 04/10/2015.
 */
class ProductController @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport  {

  val searchProductForm = Form(
    single("query" -> text)
  )

  def search = Action { implicit request =>
    val query = searchProductForm.bindFromRequest().get
    val products = Db.query[Product].whereLike("description", s"%$query%").fetch()
    Ok(html.product_list(searchProductForm, products))
  }

  def getProduct(id: Int) = Action {
    val product = Product.getById(id)
    Ok(Json.toJson(product))
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
        val product = Product(form.code1, Some(form.code2), form.description, unit, kind)
        val result = Product.save(product)
        Ok(Json.toJson(result))
      }
    )
  }

}
