package nl.suriani.jadeval.common.condition;

import nl.suriani.jadeval.common.internal.value.FactValue;

public abstract class Condition<T extends FactValue> {
	abstract boolean solve(T comparing);
}
