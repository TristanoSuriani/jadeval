package nl.suriani.jadeval.execution.workflow;

import java.util.List;

public class WorkflowOptions<T> {
	private TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler;
	private List<StateUpdateEventHandler<T>> stateUpdateEventHandlers;
	private WorkflowExecutionType executionType;

	WorkflowOptions(TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler, List<StateUpdateEventHandler<T>> stateUpdateEventHandlers, WorkflowExecutionType executionType) {
		this.transitionAttemptedEventHandler = transitionAttemptedEventHandler;
		this.stateUpdateEventHandlers = stateUpdateEventHandlers;
		this.executionType = executionType;
	}

	public TransitionAttemptedEventHandler<T> getTransitionAttemptedEventHandler() {
		return transitionAttemptedEventHandler;
	}

	public List<StateUpdateEventHandler<T>> getStateUpdateEventHandlers() {
		return stateUpdateEventHandlers;
	}

	public WorkflowExecutionType getExecutionType() {
		return executionType;
	}
}
