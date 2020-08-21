package nl.suriani.jadeval.decision.internal.value;

import java.math.BigDecimal;

public class NumericValue extends FactValue<BigDecimal> implements Comparable<NumericValue> {
	public NumericValue(BigDecimal value) {
		super(value);
	}

	public NumericValue(Integer value) {
		super(BigDecimal.valueOf(value));
	}

	public NumericValue(Float value) {
		super(BigDecimal.valueOf(value));
	}

	public NumericValue(Double value) {
		super(BigDecimal.valueOf(value));
	}

	public NumericValue(Long value) {
		super(BigDecimal.valueOf(value));
	}

	@Override
	public int compareTo(NumericValue o) {
		return value.compareTo(o.getValue());
	}
}
