package nl.suriani.jadeval.execution.statemachine;

import nl.suriani.jadeval.execution.shared.OnStateUpdateContextTransformer;
import nl.suriani.jadeval.execution.shared.TransitionAttemptedEventHandler;

import java.util.List;

public class StateMachineOptions<T> {
	private TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler;
	private List<OnStateUpdateContextTransformer<T>> onStateUpdateContextTransformers;

	StateMachineOptions(TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler, List<OnStateUpdateContextTransformer<T>> onStateUpdateContextTransformers) {
		this.transitionAttemptedEventHandler = transitionAttemptedEventHandler;
		this.onStateUpdateContextTransformers = onStateUpdateContextTransformers;
	}

	public TransitionAttemptedEventHandler<T> getTransitionAttemptedEventHandler() {
		return transitionAttemptedEventHandler;
	}

	public List<OnStateUpdateContextTransformer<T>> getStateUpdateEventHandlers() {
		return onStateUpdateContextTransformers;
	}
}
