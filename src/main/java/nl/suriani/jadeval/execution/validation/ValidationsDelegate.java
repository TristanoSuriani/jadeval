package nl.suriani.jadeval.execution.validation;

import nl.suriani.jadeval.symbols.value.Facts;
import nl.suriani.jadeval.models.condition.BooleanEqualityCondition;
import nl.suriani.jadeval.models.condition.Condition;
import nl.suriani.jadeval.models.condition.NumericEqualityCondition;
import nl.suriani.jadeval.models.condition.TextEqualityCondition;
import nl.suriani.jadeval.symbols.value.EmptyValue;
import nl.suriani.jadeval.symbols.value.FactValue;
import nl.suriani.jadeval.models.JadevalModel;
import nl.suriani.jadeval.models.Rule;

public class ValidationsDelegate {
	private JadevalModel model;

	public ValidationsDelegate(JadevalModel model) {
		this.model = model;
	}

	public void apply(Facts facts) {
		model.getRuleSet().getRules().forEach(rule -> checkConditions(rule, facts));
	}

	private void checkConditions(Rule rule, Facts facts) {
		rule.getConditions().forEach(condition -> {
			FactValue actualValue = facts.getFact(condition.getFactName())
					.orElse(new EmptyValue());
			if (!condition.solve(actualValue)) {
				throw new ValidationException(getValidationErrorMessage(rule, condition, actualValue));
			}
		});
	}

	private String getValidationErrorMessage(Rule rule, Condition condition, FactValue actualValue) {
		String message = rule.getDescription().equals("") ?
				"" : "Validation '" + rule.getDescription() + "':\n";
		return message + actualValue.getValue() + " " + getSymbol(condition) + " " + condition.getExpectedValue().getValue() +
				" (" + condition.getFactName() + ")";
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
