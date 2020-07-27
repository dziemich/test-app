package dziemich.calculator

object ValidationError extends Enumeration {
  type ErrorMessage = Value
  val INPUT_TOO_SHORT: ErrorMessage = Value("The input expression is too short.")
  val ILLEGAL_BEGINNING: ErrorMessage = Value("The input expression starts with an illegal character.")
  val ILLEGAL_ENDING: ErrorMessage = Value("The input expression ends with an illegal character.")
  val ILLEGAL_CHARACTER: ErrorMessage = Value("The input expression contains an illegal character.")
  val UNBALANCED_PARENTHESES: ErrorMessage = Value("The parentheses in the input expression are not balanced")
  val MISSING_OPERATOR: ErrorMessage = Value("There is a missing operator in the input expression")
  val INVALID_OPERATOR_USAGE: ErrorMessage = Value("The input expression contains invalid usage of operators")
}

object GlobalTypes {
  type CalculationResult = Either[Exception, Long]
  type ValidationResult = Either[ValidationError.Value, String]
}

