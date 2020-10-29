package nl.suriani.jadeval.execution.shared;

public abstract class BaseOnStateUpdateContextTransformer<T> implements OnStateUpdateContextTransformer<T> {
	private String stateName;

	public BaseOnStateUpdateContextTransformer(String stateName) {
		this.stateName = stateName;
	}

	@Override
	public T enterState(T context) {
		// no op
		return context;
	}

	@Override
	public T exitState(T context) {
		return context;
	}

	@Override
	public String getStateName() {
		return stateName;
	}
}
