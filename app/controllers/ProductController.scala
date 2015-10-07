package controllers

import javax.inject.Inject

import models.{ProductNew, ProductType, Db, Product}
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi, Messages, Lang}
import play.api.mvc.{Controller, Action}
import play.api.libs.json.Json
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

  def createProduct = Action { implicit request =>

    newProductForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(html.product_new(formWithErrors))
      },
      data => {
        val newProduct = Product(data.code1, data.code2, data.description)
        val result = Db.save(newProduct)
        Redirect(routes.ProductController.index()).flashing("success" -> s"Product ${result.id} creado!")
      }
    )

  }

}
