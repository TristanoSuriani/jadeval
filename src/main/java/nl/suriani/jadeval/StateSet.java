package nl.suriani.jadeval;

import java.util.ArrayList;
import java.util.List;

public class StateSet {
	private List<String> states;

	public StateSet(List<String> states) {
		this.states = states;
	}

	public StateSet() {
		this.states = new ArrayList<>();
	}

	public List<String> getStates() {
		return states;
	}
}
