package controllers

import model.{Db, Product}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Controller, Action}
import play.api.libs.json.Json
import sorm.mappings.Mapping
import play.api.data.format.Formats._

/**
 * Created by developer on 04/10/2015.
 */
class ProductController extends Controller {

  def newProduct = Action {

    Ok(views.html.product_new())

  }

}
