package nl.suriani.jadeval.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class ValidationsTest {
	private static final Logger log = Logger.getLogger(ValidationsTest.class.getName());

	@Test
	void testValidationsPass() {
		List<Validation> validationList = new ArrayList<>();

		Validation validationThatAlwaysPass = new Validation("Test", o -> true, o -> true, ValidationException::new);
		Validation validationThatWouldPassButIsNotExecuted = new Validation("Test", o -> false, o -> true, ValidationException::new);
		Validation validationThatWouldNotPassButIsNotExecuted = new Validation("Test", o -> false, o -> false, ValidationException::new);

		validationList.add(validationThatAlwaysPass);
		validationList.add(validationThatWouldPassButIsNotExecuted);
		validationList.add(validationThatWouldNotPassButIsNotExecuted);

		Validations validations = new Validations(validationList);
		Assertions.assertDoesNotThrow(() -> validations.apply(new Object()));
	}

	@Test
	void testValidationsFail() {
		List<Validation> validationList = new ArrayList<>();

		Validation validationThatFails = new Validation("Test", o -> true, o -> false, ValidationException::new);

		validationList.add(validationThatFails);

		Validations validations = new Validations(validationList);
		Assertions.assertThrows(ValidationException.class, () -> validations.apply(new Object()));
	}
}
