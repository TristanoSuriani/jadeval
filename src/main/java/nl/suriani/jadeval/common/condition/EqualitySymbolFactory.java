package nl.suriani.jadeval.common.condition;

public class EqualitySymbolFactory {
	public NumericEqualitySymbol getNumericEqualitySymbol(String text) {
		switch (text) {
			case "is":
			case "==":
				return NumericEqualitySymbol.IS;
			case "is not":
			case "!=":
				return NumericEqualitySymbol.IS_NOT;
			case ">":
				return NumericEqualitySymbol.GREATER_THAN;
			case ">=":
				return NumericEqualitySymbol.GREATER_THAN_EQUALS;
			case "<":
				return NumericEqualitySymbol.LESS_THAN;
			case "<=":
				return NumericEqualitySymbol.LESS_THAN_EQUALS;
			default:
				throw new IllegalArgumentException("Unrecognised equality symbol: " + text);
		}
	}

	public BooleanEqualitySymbol getBooleanEqualitySymbol(String text) {
		switch (text) {
			case "is":
			case "==":
				return BooleanEqualitySymbol.IS;
			case "is not":
			case "!=":
				return BooleanEqualitySymbol.IS_NOT;
			default:
				throw new IllegalArgumentException("Unrecognised equality symbol: " + text);
		}
	}

	public TextEqualitySymbol getTextEqualitySymbol(String text) {
		switch (text) {
			case "is":
			case "==":
				return TextEqualitySymbol.IS;
			case "is not":
			case "!=":
				return TextEqualitySymbol.IS_NOT;
			case "contains":
				return TextEqualitySymbol.CONTAINS;
			case "starts with":
				return TextEqualitySymbol.STARTS_WITH;
			case "ends with":
				return TextEqualitySymbol.ENDS_WITH;
			default:
				throw new IllegalArgumentException("Unrecognised equality symbol: " + text);
		}
	}
}
