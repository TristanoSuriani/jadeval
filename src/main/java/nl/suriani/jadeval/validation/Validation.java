package nl.suriani.jadeval.validation;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @param <T> the type of the object that has to be validated
 */
class Validation<T> {
	/**
	 * The name of the validation, not null
	 */
	private String name;

	/**
	 * The predicate that defines in which case the validation is applied, not null
	 */
	private Predicate<T> whenPredicate;

	/**
	 * The predicate that defines the conditions the validation has to satisfy to succeed, not null
	 */
	private Predicate<T> thenPredicate;

	/**
	 * The supplier of the exception that gets thrown when the validation fails, not null
	 */
	private Supplier<ValidationException> exceptionSupplier;

	public Validation(String name, Predicate<T> whenPredicate, Predicate<T> thenPredicate, Supplier<ValidationException> exceptionSupplier) {
		this.name = name;
		this.whenPredicate = whenPredicate;
		this.thenPredicate = thenPredicate;
		this.exceptionSupplier = exceptionSupplier;
	}

	public String getName() {
		return this.name;
	}

	public Predicate<T> getWhenPredicate() {
		return this.whenPredicate;
	}

	public Predicate<T> getThenPredicate() {
		return this.thenPredicate;
	}

	public Supplier<ValidationException> getExceptionSupplier() {
		return this.exceptionSupplier;
	}
}
