package nl.suriani.jadeval.execution.shared;

public interface TransitionAttemptedEventHandler<T> {
	public void handle(T object);
}
