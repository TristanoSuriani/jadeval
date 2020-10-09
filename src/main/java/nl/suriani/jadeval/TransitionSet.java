package nl.suriani.jadeval;

import nl.suriani.jadeval.workflow.internal.transition.Transition;

import java.util.ArrayList;
import java.util.List;

public class TransitionSet {
	private List<Transition> transitions = new ArrayList<>();

	public TransitionSet(List<Transition> transitions) {
		this.transitions = transitions;
	}

	public TransitionSet() {
	}

	public List<Transition> getTransitions() {
		return transitions;
	}
}
