package controllers

import javax.inject.Inject

import models.{ProductNew, Db, Product}
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi, Messages, Lang}
import play.api.mvc.{Controller, Action}
import play.api.libs.json.{JsString, Json}
import sorm.mappings.Mapping
import play.api.data.format.Formats._
import views._

/**
 * Created by developer on 04/10/2015.
 */
class ProductController @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport  {

  val newProductForm = Form[ProductNew] (
    mapping(
      "code1" -> nonEmptyText,
      "code2" -> text,
      "description" -> nonEmptyText
    )(ProductNew.apply)(ProductNew.unapply)
  )

  val searchProductForm = Form(
    single("query" -> text)
  )

  def index = Action { implicit request =>
    val products = Db.query[Product].fetch()
    Ok(html.product_list(searchProductForm, products))
  }

  def search = Action { implicit request =>
    val query = searchProductForm.bindFromRequest().get
    val products = Db.query[Product].whereLike("description", s"%$query%").fetch()
    Ok(html.product_list(searchProductForm, products))
  }

  def newProduct = Action {

    Ok(html.product_new(newProductForm))
  }

  def listProductsApi(all: Boolean) = Action {
    if (all) {
      val products = Product.all.fetch()
      Ok(Json.toJson(products take 10))
    } else {
      val rawMaterials = Product.rawMaterialsQuery.fetch()
      val json = Json.toJson(rawMaterials take 10)
      Ok(json)
    }
  }

  def listProducts(all: Boolean) = Action { implicit request =>
        if (all) {
          Ok(html.product_list2("Producto", "Listado", all))
        } else {
          Ok(html.product_list2("Materia Prima", "Listado", all))
        }
  }

  def createProduct = Action { implicit request =>

    newProductForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(html.product_new(formWithErrors))
      },
      data => {
        val newProduct = Product(data.code1, Some(data.code2), data.description, None, None)
        val result = Db.save(newProduct)
        Redirect(routes.ProductController.listProducts()).flashing("success" -> s"Product ${result.id} creado!")
      }
    )

  }

}
