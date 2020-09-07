package nl.suriani.jadeval.validation;

public class ValidationException extends IllegalArgumentException {
	public ValidationException(String s) {
		super(s);
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}
