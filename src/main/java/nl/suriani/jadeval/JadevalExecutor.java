package nl.suriani.jadeval;

import nl.suriani.jadeval.common.Facts;
import nl.suriani.jadeval.common.condition.BooleanEqualityCondition;
import nl.suriani.jadeval.common.condition.Condition;
import nl.suriani.jadeval.common.condition.NumericEqualityCondition;
import nl.suriani.jadeval.common.condition.TextEqualityCondition;
import nl.suriani.jadeval.common.internal.value.EmptyValue;
import nl.suriani.jadeval.common.internal.value.FactValue;
import nl.suriani.jadeval.decision.DecisionResult;
import nl.suriani.jadeval.decision.DecisionResults;
import nl.suriani.jadeval.validation.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JadevalExecutor {
	private JadevalModel model;

	public JadevalExecutor(JadevalModel model) {
		this.model = model;
	}

	public DecisionResults applyDecisions(Object... objects) {
		return applyDecisions(new Facts(objects));
	}

	public DecisionResults applyDecisions(Map<String, Object> factsMap) {
		return applyDecisions(new Facts(factsMap));
	}

	public void applyValidations(Object... objects) {
		applyValidations(new Facts(objects));
	}

	public void applyValidations(Map<String, Object> factsMap) {
		applyValidations(new Facts(factsMap));
	}

	// Decisions
	private DecisionResults applyDecisions(Facts facts) {
		DecisionResults results = new DecisionResults();
		model.getRuleSet().getRules().stream()
				.map(decision -> new DecisionResult(decision.getDescription(), getResponses(decision, facts)))
				.forEach(results::add);

		return results;
	}

	private List<String> getResponses(Rule rule, Facts facts) {
		return allConditionsAreSatisfied(rule, facts) ? rule.getResponses() : new ArrayList<>();
	}

	private boolean allConditionsAreSatisfied(Rule rule, Facts facts) {
		return rule.getConditions().stream()
				.allMatch(condition -> condition.solve(facts.getFact(condition.getFactName())
						.orElse(new EmptyValue())));
	}

	// Validations
	private void applyValidations(Facts facts) {
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
