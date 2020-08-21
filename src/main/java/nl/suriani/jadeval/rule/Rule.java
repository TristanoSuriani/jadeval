package nl.suriani.jadeval.rule;

import java.util.function.Consumer;
import java.util.function.Predicate;
/**
 * @param <T> the type of the object against which the rule is applied
 */
class Rule<T> {
	/**
	 * The name of the rule, not null
	 */
	private String name;

	/**
	 * The predicate that defines in which case the rule is applied, not null
	 */
	private Predicate<T> condition;

	/**
	 * The action that gets performed if the condition is satisfied
	 */
	private Consumer<T> action;

	public Rule(String name, Predicate<T> condition, Consumer<T> action) {
		this.name = name;
		this.condition = condition;
		this.action = action;
	}

	public boolean test(T subject) {
		return condition.test(subject);
	}

	public void apply(T subject) {
		action.accept(subject);
	}

	public String getName() {
		return this.name;
	}

	public Predicate<T> getCondition() {
		return this.condition;
	}

	public Consumer<T> getAction() {
		return this.action;
	}
}
