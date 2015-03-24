package org.querki

/**
 * Utilities for working with Scala.js.
 */
package object jsext {
  /**
   * A map of option values, which JSOptionBuilder builds up.
   */
  type OptMap = Map[String, Any]
  /**
   * An initial empty map of option values, which you use to begin building up
   * the options object.
   */
  val noOpts = Map.empty[String, Any]
}
