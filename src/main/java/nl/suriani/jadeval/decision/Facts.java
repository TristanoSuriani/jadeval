package nl.suriani.jadeval.decision;

import nl.suriani.jadeval.decision.internal.value.BooleanValue;
import nl.suriani.jadeval.decision.internal.value.NumericValue;
import nl.suriani.jadeval.decision.internal.value.TextValue;
import nl.suriani.jadeval.decision.annotations.Fact;
import nl.suriani.jadeval.decision.internal.FactContainer;
import nl.suriani.jadeval.decision.internal.value.EmptyValue;
import nl.suriani.jadeval.decision.internal.value.FactValue;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Container for collection of facts to be checked against the decision rules
 */
public class Facts {
	private List<FactContainer> facts;

	/**
	 * @param facts A collection of FactContainer object
	 */
	public Facts(List<FactContainer> facts) {
		if (facts.stream()
				.map(FactContainer::getFactName)
				.distinct()
				.count() < facts.size()) {
			throw new IllegalArgumentException("Violation: found more than one fact with the same name");
		}
		this.facts = facts;
	}

	public Facts() {
		facts = new ArrayList<>();
	}

	/**
	 * Checks if a fact with the given named exists in the current instance
	 * @param factName The fact's name, not null.
	 * @return result of the check
	 */
	public boolean contains(String factName) {
		return facts.stream()
				.anyMatch(fact -> fact.getFactName().equals(factName));
	}

	/**
	 * Retrieves a fact value object that has a certain name
	 * @param factName The fact's name, not null.
	 * @return fact value object
	 */
	public Optional<FactValue> getValue(String factName) {
		return facts.stream()
				.filter(factContainer -> factContainer.getFactValue() instanceof EmptyValue)
				.map(FactContainer::getFactValue)
				.findFirst();
	}

	/**
	 * Factory method to create a Facts object. All the fields in the given objects that are
	 * annotated with @Fact are used to create the instance.
	 * @param objects A list of objects from where the facts are extracted from the fields annoted with @Fact
	 * @return facts
	 */
	public static Facts fromObjects(Object... objects) {
		try {
			List<FactContainer> factContainers = Arrays.stream(objects)
					.flatMap(object -> getFactContainers(object).stream())
					.collect(Collectors.toList());

			return new Facts(factContainers);
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 * Factory method to create a Facts object. All the entries in the object map are used to create the instance.
	 * @param objectMap A map which contains the facts to be collected
	 * @return facts
	 */
	public static Facts fromObjectMap(Map<String, Object> objectMap) {
		List<FactContainer> factContainers = objectMap.entrySet().stream()
				.map(keyValuePair -> new FactContainer(keyValuePair.getKey(), getValue(keyValuePair.getValue())))
				.filter(factContainer -> !(factContainer.getFactValue() instanceof EmptyValue))
				.collect(Collectors.toList());

		return new Facts(factContainers);
	}

	private static List<FactContainer> getFactContainers(Object object) {
		List<FactContainer> currentFactContainers = Arrays.stream(object.getClass().getDeclaredFields())
				.filter(field -> field.isAnnotationPresent(Fact.class))
				.map(field -> new FactContainer(field.getName(), Facts.getValueFromfield(field, object)))
				.filter(factContainer -> factContainer.getFactValue() != null)
				.collect(Collectors.toList());
		return currentFactContainers;
	}

	private static FactValue getValueFromfield(Field field, Object object) {
		try {
			field.setAccessible(true);
			Object value = field.get(object);
			field.setAccessible(false);
			
			return getValue(value);
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}
	
	private static FactValue getValue(Object value) {
		if (value instanceof String) {
			return new TextValue((String) value);
		}

		if (value instanceof Enum) {
			return new TextValue(((Enum) value).name());
		}

		if (value instanceof Boolean) {
			return new BooleanValue((Boolean) value);
		}

		if (value instanceof Integer) {
			return new NumericValue((Integer) value);
		}

		if (value instanceof Float) {
			return new NumericValue((Float) value);
		}

		if (value instanceof Double) {
			return new NumericValue((Double) value);
		}

		if (value instanceof Long) {
			return new NumericValue((Long) value);
		}

		if (value instanceof BigDecimal) {
			return new NumericValue((BigDecimal) value);
		}
		
		return new EmptyValue();
	}
}
