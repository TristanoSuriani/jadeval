package nl.suriani.jadeval.models;

import nl.suriani.jadeval.models.condition.Condition;

import java.util.List;

public class Rule {
	private String description;
	private List<Condition> conditions;
	private List<String> responses;

	public Rule(String description, List<Condition> conditions, List<String> responses) {
		this.description = description;
		this.conditions = conditions;
		this.responses = responses;
	}

	public String getDescription() {
		return description;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public List<String> getResponses() {
		return responses;
	}
}
