package org.querki.jsext.dom

import org.scalajs.{dom => sjsdom}

class RichElement(val element: org.scalajs.dom.Element) extends AnyVal {
  def globalPosition(): (Double, Double) = {
    val r0 = RichElement.topLeftElement.getBoundingClientRect()
    val r1 = element.getBoundingClientRect()
    (r1.left - r0.left, r1.top - r0.top)
  }
}

object RichElement {
  // Forced into the top left corner of the page, so we have a reliable reference
  // point to measure from, even when android e.g. reports scroll-relative positions.
  private [dom] lazy val topLeftElement = {
    val div = sjsdom.document.createElement("div").asInstanceOf[sjsdom.html.Div]
    div.style.position = "absolute"
    div.style.left = "0px"
    div.style.top = "0px"
    div.style.width = "0px"
    div.style.height = "0px"
    sjsdom.document.body.appendChild(div)
    div
  }
}