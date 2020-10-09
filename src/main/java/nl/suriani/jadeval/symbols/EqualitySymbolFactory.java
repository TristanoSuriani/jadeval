package nl.suriani.jadeval.symbols;

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
			case "contains":
				return NumericEqualitySymbol.CONTAINS;
			case "does not contain":
				return NumericEqualitySymbol.DOES_NOT_CONTAIN;
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
			case "contains":
				return BooleanEqualitySymbol.CONTAINS;
			case "does not contain":
				return BooleanEqualitySymbol.DOES_NOT_CONTAIN;
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
			case "does not contain":
				return TextEqualitySymbol.DOES_NOT_CONTAIN;
			case "starts with":
				return TextEqualitySymbol.STARTS_WITH;
			case "does not start with":
				return TextEqualitySymbol.DOES_NOT_START_WITH;
			case "ends with":
				return TextEqualitySymbol.ENDS_WITH;
			case "does not end with":
				return TextEqualitySymbol.DOES_NOT_END_WITH;
			default:
				throw new IllegalArgumentException("Unrecognised equality symbol: " + text);
		}
	}

	public ListAndValueEqualitySymbol getListEqualitySymbol(String text) {
		switch (text) {
			case "is in":
				return ListAndValueEqualitySymbol.IS_IN;

			case "is not in":
				return ListAndValueEqualitySymbol.IS_NOT_IN;

			default: throw new IllegalArgumentException("Unrecognised equality symbol: " + text);
		}
	}
}
