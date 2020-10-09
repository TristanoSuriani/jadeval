package nl.suriani.jadeval.execution.workflow;

public interface StateUpdateEventHandler<T> {
	void enterState(T object);

	void exitState(T object);

	String getStateName();
}
