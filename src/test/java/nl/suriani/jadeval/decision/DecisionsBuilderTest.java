package nl.suriani.jadeval.decision;

import nl.suriani.jadeval.common.annotation.Fact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DecisionsBuilderTest {

	private TestDecisionBuilder builder;

	@BeforeEach
	void setUp() {
		builder = new TestDecisionBuilder();
	}

	@Test
	public void test() {
		FluentDecisions decisions = builder.build();
		AccountContext firstAccount = new AccountContext(19, false, 41000);
		AccountContext secondAccount = new AccountContext(17, false, 41000);
		AccountContext thirdAccount = new AccountContext(19, true, 41000);
		AccountContext fourthAccount = new AccountContext(63, false, 39999);

		assertTrue(decisions.apply(firstAccount)
				.getResponses()
				.contains("APPROVE_LOAN"));

		assertFalse(decisions.apply(firstAccount)
				.getResponses()
				.contains("OFFER_DISCOUNT_RATE"));

		assertFalse(decisions.apply(secondAccount)
				.getResponses()
				.contains("APPROVE_LOAN"));

		assertFalse(decisions.apply(secondAccount)
				.getResponses()
				.contains("OFFER_DISCOUNT_RATE"));

		assertFalse(decisions.apply(thirdAccount)
				.getResponses()
				.contains("APPROVE_LOAN"));

		assertFalse(decisions.apply(thirdAccount)
				.getResponses()
				.contains("OFFER_DISCOUNT_RATE"));

		assertFalse(decisions.apply(fourthAccount)
				.getResponses()
				.contains("APPROVE_LOAN"));

		assertTrue(decisions.apply(fourthAccount)
				.getResponses()
				.contains("OFFER_DISCOUNT_RATE"));
	}

	private class AccountContext {
		@Fact
		private int customerAge;

		@Fact
		private boolean previousBreaches;

		@Fact
		private long hisSalary;

		public AccountContext(int customerAge, boolean previousBreaches, long hisSalary) {
			this.customerAge = customerAge;
			this.previousBreaches = previousBreaches;
			this.hisSalary = hisSalary;
		}
	}

	private class TestDecisionBuilder extends DecisionsBuilder {

		@Override
		protected void compile() {
			decision("Approve loan")
					.when("customerAge")
					.greatherThan(toBigDecimal(18))
					.and("previousBreaches")
					.is(false)
					.then(Response.APPROVE_LOAN)
					.end();

			decision()
					.when("customerAge")
					.greatherThan(toBigDecimal(50))
					.and("previousBreaches")
					.is(false)
					.then("OFFER_DISCOUNT_RATE")
					.end();
		}
	}

	enum Response {
		APPROVE_LOAN, DENY_LOAN
	}
}