package nl.suriani.jadeval.execution.decision;

import junit.framework.Assert;
import nl.suriani.jadeval.annotation.ContainsFacts;
import nl.suriani.jadeval.symbols.value.Facts;
import nl.suriani.jadeval.annotation.Fact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

class FactsTest {

	@Test
	void getEmptyValueMayNotLeadToClassCastException() {
		Assertions.assertFalse(new Facts(null).getFact("inexistentKey").isPresent());
	}

	@Test
	void getExistingValueReturnsTheExpectedValue() {
		SomeFacts someFacts = new SomeFacts(true, "Frank", 123.456f);
		Facts facts = new Facts(someFacts);
		Assert.assertFalse(facts.getFact("inexistentKey").isPresent());
		Assert.assertEquals(true, facts.getFact("connected").get().getValue());
		Assert.assertEquals("Frank", facts.getFact("name").get().getValue());
		Assert.assertEquals(BigDecimal.valueOf(123.456f), facts.getFact("someNumber").get().getValue());
		Assert.assertEquals("/", facts.getFact("delimiter").get().getValue());
	}

	private class SomeFacts {
		@Fact
		private boolean connected;

		@Fact
		private String name;

		@Fact
		float someNumber;

		@ContainsFacts
		private SomeMoreFacts someMoreFacts;

		public SomeFacts(boolean connected, String name, float someNumber) {
			this.connected = connected;
			this.name = name;
			this.someNumber = someNumber;
			this.someMoreFacts = new SomeMoreFacts();
		}
	}

	private class SomeMoreFacts {
		@Fact(qualifier = "delimiter")
		private String del = "/";
	}
}
