package nl.suriani.jadeval.execution.shared;

public interface OnStateUpdateContextTransformer<T> {
	T enterState(T context);

	T exitState(T context);

	String getStateName();
}
