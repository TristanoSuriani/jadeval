package nl.suriani.jadeval.execution.statemachine;

import nl.suriani.jadeval.execution.shared.StateUpdateEventHandler;
import nl.suriani.jadeval.execution.shared.TransitionAttemptedEventHandler;

import java.util.ArrayList;
import java.util.List;

public class StateMachineOptionsBuilder<T> {
	private TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler;
	private List<StateUpdateEventHandler<T>> stateUpdateEventHandlers;

	public StateMachineOptionsBuilder<T> withTransitionAttemptedEventHandler(TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler) {
		this.transitionAttemptedEventHandler = transitionAttemptedEventHandler;
		return this;
	}

	public StateMachineOptionsBuilder<T> withStateUpdateEventHandler(StateUpdateEventHandler<T> stateUpdateEventHandler) {
		if (this.stateUpdateEventHandlers == null) {
			this.stateUpdateEventHandlers = new ArrayList<>();
		}
		this.stateUpdateEventHandlers.add(stateUpdateEventHandler);
		return this;
	}

	public StateMachineOptions<T> build() {
		if (this.transitionAttemptedEventHandler == null) {
			this.transitionAttemptedEventHandler = (t) -> {};
		}
		if (this.stateUpdateEventHandlers == null) {
			this.stateUpdateEventHandlers = new ArrayList<>();
		}
		return new StateMachineOptions<T>(transitionAttemptedEventHandler, stateUpdateEventHandlers);
	}
}
