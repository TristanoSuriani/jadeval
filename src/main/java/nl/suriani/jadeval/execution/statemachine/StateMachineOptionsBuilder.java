package nl.suriani.jadeval.execution.statemachine;

import nl.suriani.jadeval.execution.shared.OnStateUpdateContextTransformer;
import nl.suriani.jadeval.execution.shared.TransitionAttemptedEventHandler;

import java.util.ArrayList;
import java.util.List;

public class StateMachineOptionsBuilder<T> {
	private TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler;
	private List<OnStateUpdateContextTransformer<T>> onStateUpdateContextTransformers;

	public StateMachineOptionsBuilder<T> withTransitionAttemptedEventHandler(TransitionAttemptedEventHandler<T> transitionAttemptedEventHandler) {
		this.transitionAttemptedEventHandler = transitionAttemptedEventHandler;
		return this;
	}

	public StateMachineOptionsBuilder<T> withOnStateUpdateContextTransformer(OnStateUpdateContextTransformer<T> onStateUpdateContextTransformer) {
		if (this.onStateUpdateContextTransformers == null) {
			this.onStateUpdateContextTransformers = new ArrayList<>();
		}
		this.onStateUpdateContextTransformers.add(onStateUpdateContextTransformer);
		return this;
	}

	public StateMachineOptions<T> build() {
		if (this.transitionAttemptedEventHandler == null) {
			this.transitionAttemptedEventHandler = (t) -> {};
		}
		if (this.onStateUpdateContextTransformers == null) {
			this.onStateUpdateContextTransformers = new ArrayList<>();
		}
		return new StateMachineOptions<T>(transitionAttemptedEventHandler, onStateUpdateContextTransformers);
	}
}
