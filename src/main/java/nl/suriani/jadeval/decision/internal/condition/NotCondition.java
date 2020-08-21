package nl.suriani.jadeval.decision.internal.condition;

public class NotCondition implements ResolvableCondition {
	private boolean comparing;
	private boolean comparison;

	public NotCondition(boolean comparing, boolean comparison) {
		this.comparing = comparing;
		this.comparison = comparison;
	}

	@Override
	public boolean resolve() {
		return comparing != comparison;
	}
}
