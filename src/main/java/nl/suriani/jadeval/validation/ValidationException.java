package nl.suriani.jadeval.validation;

/**
 * Exception that gets thrown when a validation fails
 */
public class ValidationException extends RuntimeException {
	public ValidationException() {
		super();
	}

	public ValidationException(String message) {
		super(message);
	}
}
