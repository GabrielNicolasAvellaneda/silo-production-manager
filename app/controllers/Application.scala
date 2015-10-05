package controllers

import model.{Product, Db}
import play.api.mvc._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

}
