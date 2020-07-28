import dziemich.calculator.ValidationError
import dziemich.calculator.actors.Validator
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should

class ValidatorSpec extends AnyFunSpec with should.Matchers {
  val validator: Validator = new Validator();

  describe("Validator") {
    it("should check for too short input") {
      validator.validate("1 * ") should be(Left(ValidationError.INPUT_TOO_SHORT))
    }

    it("should check if first and last character are correct") {
      validator.validate("*123") should be(Left(ValidationError.ILLEGAL_BEGINNING))
      validator.validate("123* ") should be(Left(ValidationError.ILLEGAL_ENDING))
    }

    it("should check for illegal character in input") {
      validator.validate("1$+2") should be(Left(ValidationError.ILLEGAL_CHARACTER))
      validator.validate("0#1") should be(Left(ValidationError.ILLEGAL_CHARACTER))
    }

    it("should check for unbalanced parentheses") {
      validator.validate("0)1+1") should be(Left(ValidationError.UNBALANCED_PARENTHESES))
      validator.validate("(1+1") should be(Left(ValidationError.UNBALANCED_PARENTHESES))
      validator.validate("(1+1))") should be(Left(ValidationError.UNBALANCED_PARENTHESES))
      validator.validate("((1+1)") should be(Left(ValidationError.UNBALANCED_PARENTHESES))
    }

    it("should check for invalid operator usage") {
      validator.validate("(1+-9)") should be(Left(ValidationError.INVALID_OPERATOR_USAGE))
      validator.validate("(1+1-)") should be(Left(ValidationError.INVALID_OPERATOR_USAGE))
    }

  }

}
