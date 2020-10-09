package nl.suriani.jadeval.execution.validation;

public class ValidationException extends IllegalArgumentException {
	public ValidationException(String s) {
		super(s);
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}
