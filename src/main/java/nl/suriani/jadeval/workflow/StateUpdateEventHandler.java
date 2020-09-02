package nl.suriani.jadeval.workflow;

public abstract class StateUpdateEventHandler<T> {
	private String stateName;

	public StateUpdateEventHandler(String stateName) {
		this.stateName = stateName;
	}

	public abstract void enterState(T object);

	public abstract void exitState(T object);

	public String getStateName() {
		return stateName;
	}
}
