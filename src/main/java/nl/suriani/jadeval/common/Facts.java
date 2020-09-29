package nl.suriani.jadeval.common;

import nl.suriani.jadeval.common.annotation.Fact;
import nl.suriani.jadeval.common.internal.value.BooleanValue;
import nl.suriani.jadeval.common.internal.value.EmptyValue;
import nl.suriani.jadeval.common.internal.value.FactValue;
import nl.suriani.jadeval.common.internal.value.ListValue;
import nl.suriani.jadeval.common.internal.value.NumericValue;
import nl.suriani.jadeval.common.internal.value.TextValue;

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
	private List<FactEntry> factEntry;


	/**
	 * @param objectMap A map that contains a series of facts
	 */
	public Facts(Map<String, Object> objectMap) {
		List<FactEntry> factEntries = getFactEntriesFromMap(objectMap);
		this.factEntry = factEntries;
	}

	/**
	 * @param objects An object that contains a series of facts annotated with Fact
	 */
	public Facts(Object... objects) {
		List<FactEntry> factEntries = getFactEntriesFromAnnotatedObjects(objects);
		checkThatFactsAreNotAmbiguous(factEntries);
		this.factEntry = factEntries;
	}
	
	/**
	 * Checks if a fact with the given named exists in the current instance
	 * @param factName The fact's name, not null.
	 * @return result of the check
	 */
	public boolean contains(String factName) {
		return factEntry.stream()
				.anyMatch(fact -> fact.getFactName().equals(factName));
	}

	/**
	 * Retrieves a fact value object that has a certain name
	 * @param factName The fact's name, not null.
	 * @return fact value object
	 */
	public Optional<FactValue> getFact(String factName) {
		return factEntry.stream()
				.filter(factEntry -> factEntry.getFactName().equals(factName))
				.map(FactEntry::getFactValue)
				.findFirst();
	}

	private void checkThatFactsAreNotAmbiguous(List<FactEntry> facts) {
		if (facts.stream()
				.map(FactEntry::getFactName)
				.distinct()
				.count() < facts.size()) {
			throw new IllegalArgumentException("Violation: found more than one fact with the same name");
		}
	}
	
	private List<FactEntry> getFactEntriesFromMap(Map<String, Object> objectMap) {
		return objectMap.entrySet().stream()
				.map(keyValuePair -> new FactEntry(keyValuePair.getKey(), getFact(keyValuePair.getValue())))
				.filter(factEntry -> !(factEntry.getFactValue() instanceof EmptyValue))
				.collect(Collectors.toList());
	}

	private List<FactEntry> getFactEntriesFromAnnotatedObjects(Object... objects) {
		List<FactEntry> factEntries = new ArrayList<>();
		for (Object object: objects) {
			factEntries.addAll(getFactEntriesFromAnnotatedObject(object));
		}
		return factEntries;
	}

	private List<FactEntry> getFactEntriesFromAnnotatedObject(Object object) {
		List<FactEntry> currentFactEntries = Arrays.stream(object.getClass().getDeclaredFields())
				.filter(field -> field.isAnnotationPresent(Fact.class))
				.map(field -> new FactEntry("".equals(field.getAnnotation(Fact.class).qualifier().trim()) ?
						field.getName() : field.getAnnotation(Fact.class).qualifier().trim(),
						getValueFromField(field, object)))
				.filter(factEntry -> factEntry.getFactValue() != null)
				.collect(Collectors.toList());
		return currentFactEntries;
	}

	private FactValue getValueFromField(Field field, Object object) {
		try {
			field.setAccessible(true);
			Object value = field.get(object);
			field.setAccessible(false);
			
			return getFact(value);
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}
	
	private FactValue getFact(Object value) {
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

		if (value instanceof List) {
			return new ListValue(((List<?>) value).stream()
				.map(this::getFact)
					.collect(Collectors.toList()));
		}
		
		return new EmptyValue();
	}
}
