package de.leanovate.swaggercheck.model

import org.scalacheck.Prop.{BooleanOperators, forAll}
import org.scalacheck.{Arbitrary, Properties, Shrink}

object JsIntegerSpecification extends Properties("JsInteger") {
  property("shrink no min/max") = forAll(Arbitrary.arbitrary[BigInt].suchThat(_ != 0)) {
    value =>
      val original = JsInteger(None, None, value)

      val shrink = Shrink.shrink(original)

      shrink.nonEmpty :| "Shrink not empty" && shrink.forall {
        shrinked =>
          if (value < 0)
            shrinked.min.isEmpty && shrinked.max.isEmpty && shrinked.value > value
          else
            shrinked.min.isEmpty && shrinked.max.isEmpty && shrinked.value < value
      } :| "Shrink values valid"
  }

  property("shrink no max") = forAll(
    Arbitrary.arbitrary[BigInt].suchThat(_ != 0),
    Arbitrary.arbitrary[BigInt].suchThat(_ != 0).map(_.abs)) {
    (min, diff) =>
      val value = min + diff
      val original = JsInteger(Some(min), None, value)

      val shrink = Shrink.shrink(original)

      if (value == 0)
        shrink.isEmpty :| "Shrink empty"
      else
        shrink.nonEmpty :| "Shrink not empty" && shrink.forall {
          shrinked =>
            if (value < 0)
              shrinked.min.contains(min) && shrinked.max.isEmpty && shrinked.value > value && shrinked.value >= min
            else
              shrinked.min.contains(min) && shrinked.max.isEmpty && shrinked.value < value && shrinked.value >= min
        } :| "Shrink values valid"
  }

  property("shrink no min") = forAll(
    Arbitrary.arbitrary[BigInt].suchThat(_ != 0),
    Arbitrary.arbitrary[BigInt].suchThat(_ != 0).map(_.abs)) {
    (max, diff) =>
      val value = max - diff
      val original = JsInteger(None, Some(max), value)

      val shrink = Shrink.shrink(original)

      if (value == 0)
        shrink.isEmpty :| "Shrink empty"
      else
        shrink.nonEmpty :| "Shrink not empty" && shrink.forall {
          shrinked =>
            if (value < 0)
              shrinked.max.contains(max) && shrinked.min.isEmpty && shrinked.value > value && shrinked.value <= max
            else
              shrinked.max.contains(max) && shrinked.min.isEmpty && shrinked.value < value && shrinked.value <= max
        } :| "Shrink values valid"
  }


  property("shrink min/max") = forAll(
    Arbitrary.arbitrary[BigInt].suchThat(_ != 0),
    Arbitrary.arbitrary[BigInt].suchThat(_ != 0).map(_.abs),
    Arbitrary.arbitrary[BigInt].suchThat(_ != 0).map(_.abs)
  ) {
    (min, diff1, diff2) =>
      val max = min + diff1 + diff2
      val value = min + diff1
      val original = JsInteger(Some(min), Some(max), value)

      val shrink = Shrink.shrink(original)

      if (value == 0)
        shrink.isEmpty :| "Shrink empty"
      else
        shrink.nonEmpty :| "Shrink not empty" && shrink.forall {
          shrinked =>
            if (value < 0)
              shrinked.min.contains(min) && shrinked.max.contains(max) && shrinked.value > value && shrinked.value <= max
            else
              shrinked.min.contains(min) && shrinked.max.contains(max) && shrinked.value < value && shrinked.value <= max
        } :| "Shrink values valid"
  }
}
