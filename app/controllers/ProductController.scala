package controllers

import javax.inject.Inject

import models.{ProductItem, ProductNew, Db, Product, ProductTree}
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi, Messages, Lang}
import play.api.mvc.{Controller, Action}
import play.api.libs.json._
import views._

import scala.collection.JavaConverters._

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


  def generateTree(product:Product):ProductTree = {

    val tree = ProductTree(product, Seq())
    val productItems = Db.query[ProductItem].whereEqual("parent", product).fetch()
    productItems foreach { x =>
      tree.children = tree.children :+ generateTree(x.item)
    }

    tree
  }

   def productTreeApi(id: Int) = Action {

     val product = Db.query[Product].whereEqual("id", id).fetchOne()
     val tree = generateTree(product.get)

     Ok(Json.toJson(tree))
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
       // Redirect(routes.ProductController.listProducts()).flashing("success" -> s"Product ${result.id} creado!")
        Ok("test")
      }
    )

  }

}
