package nl.suriani.jadeval.execution.shared;

public abstract class BaseStateUpdateEventHandler<T> implements StateUpdateEventHandler<T> {
	private String stateName;

	public BaseStateUpdateEventHandler(String stateName) {
		this.stateName = stateName;
	}

	@Override
	public void enterState(T object) {
		// no op
	}

	@Override
	public void exitState(T object) {
		// no op
	}

	@Override
	public String getStateName() {
		return stateName;
	}
}
