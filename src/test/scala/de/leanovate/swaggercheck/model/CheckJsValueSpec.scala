package de.leanovate.swaggercheck.model

import org.scalatest.{MustMatchers, WordSpec}

class CheckJsValueSpec extends WordSpec with MustMatchers {
  "JsValueDeserializer" should {
    "deserialize null to JsNull" in {
      CheckJsValue.parse("null") mustEqual CheckJsNull
    }

    "deserialize integers to JsInteger" in {
      val result = CheckJsValue.parse("1234")

      result mustBe an[CheckJsInteger]
      result.asInstanceOf[CheckJsInteger].value mustEqual BigInt(1234)
      result.asInstanceOf[CheckJsInteger].min mustEqual Some(BigInt(1234))
    }

    "deserialize floats to JsNumber" in {
      val result = CheckJsValue.parse("1234.5")

      result mustBe an[CheckJsNumber]
      result.asInstanceOf[CheckJsNumber].value mustEqual BigDecimal(1234.5)
      result.asInstanceOf[CheckJsNumber].min mustEqual Some(BigDecimal(1234.5))
    }

    "deserialize strings to JsFormatterString" in {
      val result = CheckJsValue.parse( """"one piece of string"""")

      result mustBe an[CheckJsFormattedString]
      result.asInstanceOf[CheckJsFormattedString].value mustEqual "one piece of string"
    }

    "deserialize booleans to JsBoolean" in {
      CheckJsValue.parse("true") mustEqual CheckJsBoolean(true)
      CheckJsValue.parse("false") mustEqual CheckJsBoolean(false)
    }

    "deserialize arrays to JsArray" in {
      val result = CheckJsValue.parse( """[1234, 1234.5, true, "one piece of string"]""")

      result mustBe an[CheckJsArray]
      result.asInstanceOf[CheckJsArray].elements mustEqual Seq(
        CheckJsInteger.fixed(1234),
        CheckJsNumber.fixed(1234.5),
        CheckJsBoolean(true),
        CheckJsFormattedString("one piece of string")
      )
      result.asInstanceOf[CheckJsArray].minSize mustEqual Some(4)
    }

    "deserialize objects to JsObject" in {
      val result = CheckJsValue.parse( """{"one": 1234, "two": true, "three": "one piece of string"}""")

      result mustBe an[CheckJsObject]
      result.asInstanceOf[CheckJsObject].required mustEqual Set(
        "one", "two", "three"
      )
      result.asInstanceOf[CheckJsObject].fields mustEqual Map(
        "one" -> CheckJsInteger.fixed(1234),
        "two" -> CheckJsBoolean(true),
        "three" -> CheckJsFormattedString("one piece of string")
      )
    }
  }
}