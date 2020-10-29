package nl.suriani.jadeval.execution.workflow;

import nl.suriani.jadeval.execution.shared.OnStateUpdateContextTransformer;
import nl.suriani.jadeval.execution.shared.TransitionAttemptedEventHandler;

import java.util.List;

public class WorkflowOptions<T> {
	private TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler;
	private List<OnStateUpdateContextTransformer<T>> onStateUpdateContextTransformers;
	private WorkflowExecutionType executionType;

	WorkflowOptions(TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler, List<OnStateUpdateContextTransformer<T>> onStateUpdateContextTransformers, WorkflowExecutionType executionType) {
		this.transitionAttemptedEventHandler = transitionAttemptedEventHandler;
		this.onStateUpdateContextTransformers = onStateUpdateContextTransformers;
		this.executionType = executionType;
	}

	public TransitionAttemptedEventHandler<T> getTransitionAttemptedEventHandler() {
		return transitionAttemptedEventHandler;
	}

	public List<OnStateUpdateContextTransformer<T>> getStateUpdateEventHandlers() {
		return onStateUpdateContextTransformers;
	}

	public WorkflowExecutionType getExecutionType() {
		return executionType;
	}
}
