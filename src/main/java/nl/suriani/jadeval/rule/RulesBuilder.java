package nl.suriani.jadeval.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class RulesBuilder<T> {
	private List<nl.suriani.jadeval.rule.Rule<T>> decisions;

	protected RulesBuilder() {
		init();
	}

	protected Rule rule(String name) {
		return new Rule(name);
	}

	/**
	 * Initializes, compiles and return the rules to be applied
	 * @return the rules
	 */
	public Rules<T> build() {
		init();
		compile();
		return new Rules<T>(new ArrayList<>(decisions));
	}

	/**
	 * Allows to define all the rules that have to be applied
	 */
	protected abstract void compile();

	private void init() {
		decisions = new ArrayList<>();
	}

	protected class Rule {
		private String name;

		public Rule(String name) {
			this.name = name;
		}

		public When when(Predicate<T> condition) {
			return new When(name, condition);
		}
	}

	protected class When {
		private String name;
		private Predicate<T> condition;

		public When(String name, Predicate<T> condition) {
			this.name = name;
			this.condition = condition;
		}

		public When and(Predicate<T> condition) {
			return new When(name, this.condition.and(condition));
		}

		public Then then(Consumer<T> action) {
			return new Then(name, condition, action);
		}
	}

	protected class Then {
		private String name;
		private Predicate<T> condition;
		private Consumer<T> action;

		public Then(String name, Predicate<T> condition, Consumer<T> action) {
			this.name = name;
			this.condition = condition;
			this.action = action;
		}

		public Then andThen(Consumer<T> action) {
			return new Then(name, condition, this.action.andThen(action));
		}

		public void end() {
			decisions.add(new nl.suriani.jadeval.rule.Rule<>(name, condition, action));
		}
	}
}
