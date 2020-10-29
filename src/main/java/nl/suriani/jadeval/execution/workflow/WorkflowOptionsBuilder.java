package nl.suriani.jadeval.execution.workflow;

import nl.suriani.jadeval.execution.shared.OnStateUpdateContextTransformer;
import nl.suriani.jadeval.execution.shared.TransitionAttemptedEventHandler;

import java.util.ArrayList;
import java.util.List;

public class WorkflowOptionsBuilder<T> {
	private TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler;
	private List<OnStateUpdateContextTransformer<T>> onStateUpdateContextTransformers;
	private WorkflowExecutionType executionType;

	public WorkflowOptionsBuilder<T> withTransitionAttemptedEventHandler(TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler) {
		this.transitionAttemptedEventHandler = transitionAttemptedEventHandler;
		return this;
	}

	public WorkflowOptionsBuilder<T> withOnStateUpdateContextTransformer(OnStateUpdateContextTransformer<T> onStateUpdateContextTransformer) {
		if (this.onStateUpdateContextTransformers == null) {
			this.onStateUpdateContextTransformers = new ArrayList<>();
		}
		this.onStateUpdateContextTransformers.add(onStateUpdateContextTransformer);
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
		if (this.onStateUpdateContextTransformers == null) {
			this.onStateUpdateContextTransformers = new ArrayList<>();
		}
		if (this.executionType == null) {
			this.executionType = WorkflowExecutionType.ONE_TRANSITION_PER_TIME;
		}
		return new WorkflowOptions<T>(transitionAttemptedEventHandler, onStateUpdateContextTransformers, executionType);
	}
}
