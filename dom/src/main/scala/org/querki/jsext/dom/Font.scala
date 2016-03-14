package org.querki.jsext.dom

import org.scalajs.dom

/**
  * Construct a new font with the specified style
  * @param family the family name
  * @param style the style of the font
  * @param size the size of the font
  */
class Font(val family: String, val style: Font.Style, val size: Float) {
  /**
    * Returns the CSS string for this font.
    * @return the CSS string.
    */
  def toCSS: String = {
    val flags = style match {
      case Font.Plain => ""
      case Font.Bold => "bold "
      case Font.Italic => "italic "
      case Font.BoldItalic => "bold italic "
    }
    s"$flags${size}px $family"
  }

  val (ascent, descent, height): (Float, Float, Float) = {
    Font.iframe.contentWindow.document.body.appendChild(Font.div)
    Font.div.style.lineHeight = size+"px"
    Font.text.style.font = toCSS
    Font.block.style.verticalAlign = "baseline"
    Font.text.textContent = "XYZ"
    val ascent = (Font.block.offsetTop-Font.text.offsetTop).toFloat
    Font.block.style.verticalAlign = "bottom"
    val height = (Font.block.offsetTop-Font.text.offsetTop).toFloat
    (ascent, height-ascent, height)
  }

  def stringWidth(string: String): Float = {
    Font.ctx.font = toCSS
    Font.ctx.measureText(string).width.toFloat
  }
}

object Font {
  private val ctx= dom.document.createElement("canvas").asInstanceOf[dom.html.Canvas].getContext("2d").asInstanceOf[dom.raw.CanvasRenderingContext2D]

  sealed trait Style
  case object Plain extends Style
  case object Bold extends Style
  case object Italic extends Style
  case object BoldItalic extends Style

  private val text = dom.document.createElement("span").asInstanceOf[dom.html.Span]
  private val block = dom.document.createElement("div").asInstanceOf[dom.html.Div]
  block.style.display = "inline-block"
  block.style.width = "1px"
  block.style.height = "0px"


  private val iframe = dom.document.createElement("iframe").asInstanceOf[dom.html.IFrame]
  dom.document.body.appendChild(iframe)

  // in firefox, the offsetTop appears to be approximately clipped against the iframe viewport
  // we have to create a sufficiently large iframe off-screen in order to make this work.
  iframe.width = "8000"
  iframe.height = "8000"
  iframe.style.position = "absolute"
  iframe.style.left = "-8500px"
  iframe.style.top = "-8500px"
  iframe.contentWindow.document.open()
  iframe.contentWindow.document.write("<html><head><meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport'/></head><body></body></html>")
  iframe.contentWindow.document.close()
  iframe.contentWindow.document.body.style.width = "10000px"
  iframe.contentWindow.document.body.style.height = "10000px"


  private val div = dom.document.createElement("div").asInstanceOf[dom.html.Div]

  div.style.position = "absolute"
  text.style.whiteSpace = "pre"
  div.appendChild(text)
  div.appendChild(block)

  /**
    * Construct a new Font with the specified styles.
    * @param family the font family name.
    * @param style the style of the font.
    * @param size the size of the font.
    * @return a new font object.
    */
  def apply(family: String, style: Style, size: Float) =
    new Font(family, style, size)
}


