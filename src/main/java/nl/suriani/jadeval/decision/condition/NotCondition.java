package nl.suriani.jadeval.decision.condition;

public class NotCondition implements Condition {
	private boolean comparing;
	private boolean comparison;

	public NotCondition(boolean comparing, boolean comparison) {
		this.comparing = comparing;
		this.comparison = comparison;
	}

	@Override
	public boolean solve() {
		return comparing != comparison;
	}
}
