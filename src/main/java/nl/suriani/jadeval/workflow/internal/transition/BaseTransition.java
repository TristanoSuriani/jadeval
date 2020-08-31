package nl.suriani.jadeval.workflow.internal.transition;

public class BaseTransition implements Transition {
	private String fromState;
	private String toState;

	public BaseTransition(String fromState, String toState) {
		this.fromState = fromState;
		this.toState = toState;
	}
}
