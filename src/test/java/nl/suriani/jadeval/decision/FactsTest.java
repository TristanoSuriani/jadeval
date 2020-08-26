package nl.suriani.jadeval.decision;

import junit.framework.Assert;
import nl.suriani.jadeval.decision.annotations.Fact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

class FactsTest {

	@Test
	void getEmptyValueMayNotLeadToClassCastException() {
		Assertions.assertFalse(new Facts().getFact("inexistentKey").isPresent());
	}

	@Test
	void getExistingValueReturnsTheExpectedValue1() {
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put("aKey", "aValue");
		Facts facts = new Facts(factsMap);
		Assert.assertTrue(facts.getFact("aKey").isPresent());
		Assert.assertEquals("aValue", facts.getFact("aKey").get().getValue());
	}

	@Test
	void getExistingValueReturnsTheExpectedValue2() {
		SomeFacts someFacts = new SomeFacts(true, "Frank", 123.456f);
		Facts facts = new Facts(someFacts);
		Assert.assertFalse(facts.getFact("inexistentKey").isPresent());
		Assert.assertEquals(true, facts.getFact("connected").get().getValue());
		Assert.assertEquals("Frank", facts.getFact("name").get().getValue());
		Assert.assertEquals(BigDecimal.valueOf(123.456f), facts.getFact("someNumber").get().getValue());
	}

	private class SomeFacts {
		@Fact
		private boolean connected;

		@Fact
		private String name;

		@Fact
		float someNumber;

		public SomeFacts(boolean connected, String name, float someNumber) {
			this.connected = connected;
			this.name = name;
			this.someNumber = someNumber;
		}
	}
}
