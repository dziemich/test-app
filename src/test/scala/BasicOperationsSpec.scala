import dziemich.calculator.utils.BasicOperations
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class BasicOperationsSpec extends AnyFlatSpec with should.Matchers {
  "Basic Operations" should "compute addition correctly" in {
    BasicOperations.calculate(2, 2, '+') should be(Right(2 + 2))
  }

  it should "compute subtraction correctly" in {
    BasicOperations.calculate(2, 2, '-') should be(Right(2 - 2))
    BasicOperations.calculate(-2, 2, '-') should be(Right(-2 - 2))

  }

  it should "compute multiplication correctly" in {
    BasicOperations.calculate(2, 2, '*') should be(Right(2 * 2))
    BasicOperations.calculate(2, -2, '*') should be(Right(2 * -2))
  }

  it should "compute division correctly" in {
    BasicOperations.calculate(2, 2, '/') should be(Right(2 / 2))
    BasicOperations.calculate(2, -2, '/') should be(Right(2 / -2))
  }
}
