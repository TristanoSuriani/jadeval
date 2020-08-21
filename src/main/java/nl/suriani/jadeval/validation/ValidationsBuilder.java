package nl.suriani.jadeval.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ValidationsBuilder<T> {

	private static final Logger log = Logger.getLogger(ValidationsBuilder.class.getName());
	private List<nl.suriani.jadeval.validation.Validation<T>> validations;
	private Logger logger = log;
	private Level logLevel = Level.FINE;

	public ValidationsBuilder() {
		init();
	}

	/**
	 * Allows to define all the validations that have to be applied
	 */
	public abstract void compile();

	protected void setLogger(Logger logger) {
		this.logger = logger;
	}

	protected void setLogLevel(Level logLevel) {
		this.logLevel = logLevel;
	}

	/**
	 * Initializes, compiles and return the validations to be applied
	 * @return the validations
	 */
	public Validations build() {
		init();
		compile();
		return new Validations(validations, logger, logLevel);
	}

	/**
	 * Starts the declaration of a validation adding its name
	 * @param name name of the validation (for example: <i>"A person must be 18 to order alcohol"</i>)
	 * @return validation fluent api object
	 */
	protected Validation validation(String name) {
		return new Validation(name);
	}

	/**
	 * returns a predicate that is always true
	 * @return t Always returns true
	 */
	protected Predicate<T> always() {
		return t -> true;
	}

	/**
	 * returns a predicate that checks if the current facts are of the given type
	 * @param clazz class of the given type
	 * @return predicate
	 */
	protected Predicate<T> itIsOfType(Class clazz) {
		return t -> clazz.isAssignableFrom(t.getClass());
	}

	protected class Validation {
		private String name;

		public Validation(String name) {
			this.name = name;
		}

		/**
		 * Starts the declaration of a validation adding its name and re
		 * @param whenPredicate predicate that defines in which case the validation is applied, not null
		 * @return When fluent api object
		 */
		public When when(Predicate<T> whenPredicate) {
			return new When(name, whenPredicate);
		}
	}

	protected class When {
		private String name;
		private Predicate<T> whenPredicate;

		public When(String name, Predicate<T> whenPredicate) {
			this.name = name;
			this.whenPredicate = whenPredicate;
		}

		/**
		 * Returns Then fluent api which allows to define
		 * @param thenPredicate predicate that defines the conditions the validation has to satisfy to succeed, not null
		 * @return Then fluent api object
		 */
		public Then then(Predicate<T> thenPredicate) {
			return new Then(name, whenPredicate, thenPredicate);
		}

		/**
		 * Allows to compose the existing whenPredicate with a new whenPredicate
		 * @param andPredicate predicate that is composed to the existing whenPredicate to define in which case the validation is applied, not null
		 * @return Then fluent api object
		 */
		public When and(Predicate<T> andPredicate) {
			return new When(name, whenPredicate.and(andPredicate));
		}
	}

	protected class Then {
		private String name;
		private Predicate<T> whenPredicate;
		private Predicate<T> thenPredicate;

		public Then(String name, Predicate<T> whenPredicate, Predicate<T> thenPredicate) {
			this.name = name;
			this.whenPredicate = whenPredicate;
			this.thenPredicate = thenPredicate;
		}

		/**
		 * Allows to define a supplier for a validation exception
		 * @param exceptionSupplier supplier of the exception that is thrown when the validation fails, not null
		 */
		public void orElseThrow(Supplier<ValidationException> exceptionSupplier) {
			validations.add(new nl.suriani.jadeval.validation.Validation<T>(name, whenPredicate, thenPredicate, exceptionSupplier));
		}

		/**
		 * Allows to compose the existing thenPredicate with a new thenPredicate
		 * @param andPredicate predicate that is composed to the existing thenPredicate to the conditions the validation has to satisfy to succeed, not null
		 * @return Then fluent api object
		 */
		public Then and(Predicate<T> andPredicate) {
			return new Then(name, whenPredicate, thenPredicate.and(andPredicate));
		}
	}

	private void init() {
		validations = new ArrayList<>();
	}
}
