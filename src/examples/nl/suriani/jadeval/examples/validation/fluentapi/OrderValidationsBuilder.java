package nl.suriani.jadeval.examples.validation.fluentapi;

import nl.suriani.jadeval.validation.ValidationsFluentBuilder;

public class OrderValidationsBuilder extends ValidationsFluentBuilder {
	@Override
	protected void compile() {
		validation("The amount of product must be in stock")
				.isValidWhen("amount")
				.greatherThanEquals(toBigDecimal(1))
				.and("itemsInStockGreaterThanAmount")
				.is(true)
				.end();

		validation("The order cannot be processed for customers with a blocked account")
				.isValidWhen("blockedAccount")
				.is(false)
				.end();
	}
}
