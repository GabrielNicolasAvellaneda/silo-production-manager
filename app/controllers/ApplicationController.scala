package controllers

import javax.inject.Inject

import play.api.mvc.{Action, Controller}
import play.api.i18n.{I18nSupport, MessagesApi, Messages, Lang}
import views._

/**
 * Created by developer on 19/10/2015.
 */
class ApplicationController @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport  {
  def index = Action {
    Ok(html.index())
  }

  def catchAll(path: String) = Action {
    Redirect("/")
  }
}
