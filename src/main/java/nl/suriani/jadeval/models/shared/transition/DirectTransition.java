package nl.suriani.jadeval.models.shared.transition;

public class DirectTransition implements Transition {
	private String fromState;
	private String toState;

	public DirectTransition(String fromState, String toState) {
		this.fromState = fromState;
		this.toState = toState;
	}

	public String getFromState() {
		return fromState;
	}

	public String getToState() {
		return toState;
	}
}
