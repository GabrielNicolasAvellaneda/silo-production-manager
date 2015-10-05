package model

import sorm._

/**
 * Created by developer on 04/10/2015.
 */
object Db extends Instance(entities = Seq(Entity[Product]()), url = "jdbc:h2:mem:test")

