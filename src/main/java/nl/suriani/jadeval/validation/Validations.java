package nl.suriani.jadeval.validation;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Object that wraps the validations and allow to apply them
 * @param <T> The type of the facts to be validated
 */
public class Validations<T> {
	private static final Logger log = Logger.getLogger(Validations.class.getName());
	/**
	 * Validations to be performed, not null
	 */
	private List<Validation<T>> validations;

	/**
	 * Logger, not null
	 */
	private Logger logger = log;

	/**
	 * Log level
	 */
	private Level logLevel = Level.FINE;

	public Validations(List<Validation<T>> validations) {
		this.validations = validations;
	}

	public Validations(List<Validation<T>> validations, Logger logger, Level logLevel) {
		this.validations = validations;
		this.logger = logger;
		this.logLevel = logLevel;
	}

	/**
	 * Applies the validations to the facts
	 * @param facts facts to be checked
	 */
	public void apply(T facts) {
		List<Validation> whenPredicateMatchingValidations = validations.stream()
				.filter(validation -> validation.getWhenPredicate().test(facts))
				.collect(Collectors.toList());

		whenPredicateMatchingValidations.stream()
				.filter(validation -> !validation.getThenPredicate().test(facts))
				.findFirst()
				.ifPresent(this::logAndThrowValidationException);
	}

	private void logAndThrowValidationException(Validation<T> validation) {
		ValidationException validationException = validation.getExceptionSupplier().get();
		logger.log(logLevel, "Failed validation: " + validation.getName() + "\nMessage: "+ validationException.getMessage());
		throw validationException;
	}
}
