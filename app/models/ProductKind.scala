package models

import play.api.libs.json.{OWrites, Writes}

/**
 * Created by developer on 05/10/2015.
 */
case class ProductKind (name:String, description: String) {

}

object ProductKind extends LowPriorityWriteInstances {

  implicit val productKindPersistsedWrites = new Writes[ProductKind with sorm.Persisted] {
    def writes(o: ProductKind with sorm.Persisted) =
      productKindWrites.writes(o) ++ implicitly[OWrites[sorm.Persisted]].writes(o)
  }

  def getById(id: Long) = Db.query[ProductKind].whereEqual("id", id).fetchOne()

}
