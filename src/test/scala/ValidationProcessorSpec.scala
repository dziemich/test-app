import dziemich.calculator.processing.ValidationProcessor
import dziemich.calculator.utils.ProcessingError
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should

class ValidationProcessorSpec extends AnyFunSpec with should.Matchers {

  describe("ValidationProcessor") {
    it("should check for too short input") {
      ValidationProcessor.validate("1 * ") should be(Left(ProcessingError.INPUT_TOO_SHORT))
    }

    it("should check if first and last character are correct") {
      ValidationProcessor.validate("*123") should be(Left(ProcessingError.ILLEGAL_BEGINNING))
      ValidationProcessor.validate("123* ") should be(Left(ProcessingError.ILLEGAL_ENDING))
    }

    it("should check for illegal character in input") {
      ValidationProcessor.validate("1$+2") should be(Left(ProcessingError.ILLEGAL_CHARACTER))
      ValidationProcessor.validate("0#1") should be(Left(ProcessingError.ILLEGAL_CHARACTER))
    }

    it("should check for unbalanced parentheses") {
      ValidationProcessor.validate("0)1+1") should be(Left(ProcessingError.UNBALANCED_PARENTHESES))
      ValidationProcessor.validate("(1+1") should be(Left(ProcessingError.UNBALANCED_PARENTHESES))
      ValidationProcessor.validate("(1+1))") should be(Left(ProcessingError.UNBALANCED_PARENTHESES))
      ValidationProcessor.validate("((1+1)") should be(Left(ProcessingError.UNBALANCED_PARENTHESES))
    }

    it("should check for invalid operator usage") {
      ValidationProcessor.validate("(1+-9)") should be(Left(ProcessingError.INVALID_OPERATOR_USAGE))
      ValidationProcessor.validate("(1+1-)") should be(Left(ProcessingError.INVALID_OPERATOR_USAGE))
    }

  }

}
