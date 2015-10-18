package models

import sorm._

/**
 * Created by developer on 04/10/2015.
 */
object Db extends Instance(
  entities = Seq(Entity[Product](), Entity[ProductItem](), Entity[ProductKind](), Entity[ProductUnit]()),
  url = "jdbc:mysql://localhost:3306/silofabric",
  user = "developer",
  initMode = InitMode.DoNothing
) {




}

