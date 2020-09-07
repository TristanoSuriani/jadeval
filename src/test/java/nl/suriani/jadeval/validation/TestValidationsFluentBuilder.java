package nl.suriani.jadeval.validation;

public class TestValidationsFluentBuilder extends ValidationsFluentBuilder {
	private final static int THRESHOLD = 3;
	private final static int MAXIMUM_RATE = 10;

	@Override
	protected void compile() {
		validation("The amount cannot be greater than the threshold")
				.isValidWhen("amount")
				.lessThanEquals(toBigDecimal(THRESHOLD))
				.end();

		validation()
				.isValidWhen("age")
				.greatherThanEquals(toBigDecimal(18))
				.and("deposit")
				.greatherThanEquals(toBigDecimal(2000))
				.end();

		validation()
				.isValidWhen("code")
				.contains("NLBANK")
				.and("code")
				.startsWith("123")
				.and("code")
				.endsWith("000")
				.end();

		validation()
				.isValidWhen("code")
				.isNot("empty")
				.and("operationAllowed")
				.is(true)
				.end();

		validation()
				.isValidWhen("status")
				.isNot(toBigDecimal(1))
				.end();

		validation()
				.isValidWhen("rate")
				.lessThanEquals(toBigDecimal(MAXIMUM_RATE))
				.and("operationAllowed")
				.is(true)
				.end();

	}
}
