package nl.suriani.jadeval.execution.workflow;

public interface TransitionAttemptedEventHandler<T> {
	public void handle(T object);
}
