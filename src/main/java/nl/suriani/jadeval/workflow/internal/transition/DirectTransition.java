package nl.suriani.jadeval.workflow.internal.transition;

public class DirectTransition implements Transition {
	private String fromState;
	private String toState;

	public DirectTransition(String fromState, String toState) {
		this.fromState = fromState;
		this.toState = toState;
	}
}
