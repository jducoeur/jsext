package org.querki.jsext

import language.implicitConversions

/**
  * Created by Matt on 14/03/2016.
  */
package object dom {
  implicit def enrichElement(elem: org.scalajs.dom.Element): RichElement =
    new RichElement(elem)
}
