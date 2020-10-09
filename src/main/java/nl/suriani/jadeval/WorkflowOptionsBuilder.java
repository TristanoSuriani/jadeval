package nl.suriani.jadeval;

import nl.suriani.jadeval.workflow.StateUpdateEventHandler;
import nl.suriani.jadeval.workflow.TransitionAttemptedEventHandler;

import java.util.ArrayList;
import java.util.List;

public class WorkflowOptionsBuilder<T> {
	private TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler;
	private List<StateUpdateEventHandler<T>> stateUpdateEventHandlers;
	private WorkflowExecutionType executionType;

	public WorkflowOptionsBuilder<T> withTransitionAttemptedEventHandler(TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler) {
		this.transitionAttemptedEventHandler = transitionAttemptedEventHandler;
		return this;
	}

	public WorkflowOptionsBuilder<T> withStateUpdateEventHandler(StateUpdateEventHandler<T> stateUpdateEventHandler) {
		if (this.stateUpdateEventHandlers == null) {
			this.stateUpdateEventHandlers = new ArrayList<>();
		}
		this.stateUpdateEventHandlers.add(stateUpdateEventHandler);
		return this;
	}

	public WorkflowOptionsBuilder<T> withExecutionType(WorkflowExecutionType executionType) {
		this.executionType = executionType;
		return this;
	}

	public WorkflowOptions<T> build() {
		if (this.transitionAttemptedEventHandler == null) {
			this.transitionAttemptedEventHandler = (t) -> {};
		}
		if (this.stateUpdateEventHandlers == null) {
			this.stateUpdateEventHandlers = new ArrayList<>();
		}
		if (this.executionType == null) {
			this.executionType = WorkflowExecutionType.ONE_TRANSITION_PER_TIME;
		}
		return new WorkflowOptions<T>(transitionAttemptedEventHandler, stateUpdateEventHandlers, executionType);
	}
}
