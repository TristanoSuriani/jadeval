package nl.suriani.jadeval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkflowStateSet extends StateSet {
	private List<String> rootStates;
	private List<String> intermediateStates;
	private List<String> finalStates;

	public WorkflowStateSet(List<String> rootStates, List<String> intermediateStates, List<String> finalStates) {
		super(new ArrayList<>());
		this.rootStates = rootStates;
		this.intermediateStates = intermediateStates;
		this.finalStates = finalStates;
		this.getStates().addAll(concat(rootStates, intermediateStates, finalStates));
	}

	private List<String> concat(List<String>... stateList) {
		List<String> states = new ArrayList<>();
		Arrays.asList(stateList).forEach(states::addAll);
		return states;
	}
}
