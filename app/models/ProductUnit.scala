package models

import play.api.libs.json.{OWrites, Writes}

/**
 * Created by developer on 18/10/2015.
 */
case class ProductUnit (name: String, description: String) {

}

object ProductUnit extends LowPriorityWriteInstances {

 implicit val productUnitPersistedWrites = new Writes[ProductUnit with sorm.Persisted] {
  def writes(o: ProductUnit with sorm.Persisted) =
   productUnitWrites.writes(o) ++ implicitly[OWrites[sorm.Persisted]].writes(o)
 }

}
