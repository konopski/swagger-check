package de.leanovate.swaggercheck

/**
 * Generic trait to verify something.
 */
trait Verifier[T] {
  /**
   * Verifies that a value matches a criteria.
   */
  def verify(value: T): VerifyResult
}
