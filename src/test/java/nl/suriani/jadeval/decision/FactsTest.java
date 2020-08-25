package nl.suriani.jadeval.decision;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactsTest {

	@Test
	void getEmptyValueMayNotLeadToClassCastException() {
		Assertions.assertFalse(Facts.fromObjects().getValue("inexistentKey").isPresent());
	}
}
