package nl.suriani.jadeval.execution.statemachine;

import nl.suriani.jadeval.execution.shared.StateUpdateEventHandler;
import nl.suriani.jadeval.execution.shared.TransitionAttemptedEventHandler;

import java.util.List;

public class StateMachineOptions<T> {
	private TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler;
	private List<StateUpdateEventHandler<T>> stateUpdateEventHandlers;

	StateMachineOptions(TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler, List<StateUpdateEventHandler<T>> stateUpdateEventHandlers) {
		this.transitionAttemptedEventHandler = transitionAttemptedEventHandler;
		this.stateUpdateEventHandlers = stateUpdateEventHandlers;
	}

	public TransitionAttemptedEventHandler<T> getTransitionAttemptedEventHandler() {
		return transitionAttemptedEventHandler;
	}

	public List<StateUpdateEventHandler<T>> getStateUpdateEventHandlers() {
		return stateUpdateEventHandlers;
	}
}
