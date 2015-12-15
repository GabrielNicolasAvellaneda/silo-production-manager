package models

import play.api.libs.json.Json
/**
 * Created by developer on 15/12/2015.
 */
case class DashboardData (rawMaterials: Int, assemblies: Int, silos: Int, finishedProducts: Int)

object DashboardData {
  implicit val updateProductFormFormat = Json.format[DashboardData]
}
