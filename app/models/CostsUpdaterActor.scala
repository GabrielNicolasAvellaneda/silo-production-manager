package models

import akka.actor.{Props, ActorLogging, Actor}

/**
 * Created by developer on 09/11/2015.
 */
class CostsUpdaterActor extends Actor with ActorLogging {

  import CostsUpdaterActor._

  def receive = {

    case UpdateCostsMessage(product) =>
      log.info("Start updateCostsForItem for {}", product.code1)
      Product.updateCostsForItem(product)
      log.info("End updateCostsForItem for {}", product.code1)
  }

}

object CostsUpdaterActor {
  val props = Props[CostsUpdaterActor]

  case class UpdateCostsMessage(product: Product)
}
