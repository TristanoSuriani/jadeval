package nl.suriani.jadeval.workflow;

public interface TransitionAttemptedEventHandler<T> {
	public void handle(T object);
}
