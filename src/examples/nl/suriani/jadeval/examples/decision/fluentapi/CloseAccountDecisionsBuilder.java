package nl.suriani.jadeval.examples.decision.fluentapi;

import nl.suriani.jadeval.decision.DecisionsBuilder;

import java.math.BigDecimal;

public class CloseAccountDecisionsBuilder extends DecisionsBuilder {
	private static final BigDecimal MINIMUM_AGE = BigDecimal.valueOf(18);

	@Override
	protected void compile() {
		decision()
				.when("age")
				.greatherThanEquals(MINIMUM_AGE)
				.and("amount")
				.greatherThan(toBigDecimal(0))
				.then("CLOSE_ACCOUNT")
				.and("SEND_CONFIRMATION_LETTER")
				.end();

		decision()
				.when("customStuff")
				.is("custom")
				.then("DEFAULT_DESCRIPTION")
				.end();
	}
}
