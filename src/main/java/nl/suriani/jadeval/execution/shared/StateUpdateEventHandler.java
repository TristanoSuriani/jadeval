package nl.suriani.jadeval.execution.shared;

public interface StateUpdateEventHandler<T> {
	void enterState(T object);

	void exitState(T object);

	String getStateName();
}
