package nl.suriani.jadeval.validation;

import nl.suriani.jadeval.common.condition.Condition;

import java.util.List;

public class Validation {
	private String description;

	private List<Condition> conditions;

	Validation(String description, List<Condition> conditions) {
		this.description = description;
		this.conditions = conditions;
	}

	public String getDescription() {
		return description;
	}

	public List<Condition> getConditions() {
		return conditions;
	}
}
