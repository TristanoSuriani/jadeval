package nl.suriani.jadeval.validation;

import nl.suriani.jadeval.common.annotation.Fact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class ValidationsTest {

	@Fact
	private int age;

	@Fact
	private long amount;

	@Fact
	private float rate;

	@Fact
	private String code;

	@Fact
	private BigDecimal deposit;

	@Fact
	private boolean operationAllowed = false;

	@Fact
	private int status = 2;

	@BeforeEach
	void setUp() {
		age = 19;
		amount = 10;
		rate = 3.15f;
		code = "123NLBANKF123000";
		deposit = BigDecimal.valueOf(10000);
	}

	@Test
	void testWithJVL() {
		Validations validations = ValidationsBuilder.newFromFile(new File("src/test/resources/validations.jvl"))
				.build();

		Exception exception = Assertions.assertThrows(ValidationException.class, () -> {
			validations.apply(this);
		});
		Assertions.assertTrue(exception.getMessage().contains("10 LESS_THAN_EQUALS 3.0 (amount)"));

		amount = 2;
		age = 31;
		exception = Assertions.assertThrows(ValidationException.class, () -> {
			validations.apply(this);
		});
		Assertions.assertTrue(exception.getMessage().contains("false IS true (operationAllowed)"));

		operationAllowed = true;

		validations.apply(this);
	}

	@Test
	void testWithFluentBuilder() {
		Validations validations = new TestValidationsFluentBuilder().build();

		Exception exception = Assertions.assertThrows(ValidationException.class, () -> {
			validations.apply(this);
		});
		Assertions.assertTrue(exception.getMessage().contains("10 LESS_THAN_EQUALS 3.0 (amount)"));

		amount = 2;
		age = 31;
		exception = Assertions.assertThrows(ValidationException.class, () -> {
			validations.apply(this);
		});
		Assertions.assertTrue(exception.getMessage().contains("false IS true (operationAllowed)"));

		operationAllowed = true;

		validations.apply(this);
	}
}
