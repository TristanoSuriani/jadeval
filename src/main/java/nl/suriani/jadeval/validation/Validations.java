package nl.suriani.jadeval.validation;

import nl.suriani.jadeval.common.Facts;
import nl.suriani.jadeval.common.condition.BooleanEqualityCondition;
import nl.suriani.jadeval.common.condition.Condition;
import nl.suriani.jadeval.common.condition.NumericEqualityCondition;
import nl.suriani.jadeval.common.condition.TextEqualityCondition;
import nl.suriani.jadeval.common.internal.value.EmptyValue;

import java.util.List;
import java.util.Map;

public class Validations {
	private List<Validation> validations;

	public Validations(List<Validation> validations) {
		this.validations = validations;
	}

	public void apply(Object... objects) {
		apply(new Facts(objects));
	}

	public void apply(Map<String, Object> objects) {
		apply(new Facts(objects));
	}

	private void apply(Facts facts) {
		validations.forEach(validation -> checkConditions(validation, facts));
	}

	private void checkConditions(Validation validation, Facts facts) {
		validation.getConditions().forEach(condition -> {
			if (!condition.solve(facts.getFact(condition.getFactName())
					.orElse(new EmptyValue()))) {

				throw new ValidationException(getValidationErrorMessage(validation, condition));
			}
		});
	}

	private String getValidationErrorMessage(Validation validation, Condition condition) {
		String message = validation.getDescription().equals("") ?
				"" : "Validation '" + validation.getDescription() + "': ";
		return message + condition.getComparing() + " " + getSymbol(condition) + condition.getComparing();
	}

	private String getSymbol(Condition condition) {
		if (condition instanceof NumericEqualityCondition) {
			return ((NumericEqualityCondition) condition).getSymbol().name();
		} else if (condition instanceof BooleanEqualityCondition) {
			return ((BooleanEqualityCondition) condition).getSymbol().name();
		} else if (condition instanceof TextEqualityCondition) {
			return ((TextEqualityCondition) condition).getSymbol().name();
		} else {
			return "";
		}
	}
}
