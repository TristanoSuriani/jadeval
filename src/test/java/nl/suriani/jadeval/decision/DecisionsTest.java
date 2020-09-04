package nl.suriani.jadeval.decision;

import nl.suriani.jadeval.common.Facts;
import nl.suriani.jadeval.common.annotation.Fact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

class DecisionsTest {

	private Decisions runner;

	@Fact
	private double amount;

	@Fact
	private boolean disconnected;

	@Fact
	private boolean connected;

	@Fact
	private double credit;

	@BeforeEach
	void setUp() {
		runner = new Decisions();
		amount = 0.1;
		disconnected = true;
		connected = true;
		credit = 2000.01;
	}

	@Test
	void runFromFileName() {
		String fileName = "src/test/resources/decisions.jdl";
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put("connected", false);
		factsMap.put("credit", 2000);

		/*
			Matching rule:
			"This rule has a description"
			when connected is false
				and credit > 1234.56                // This is a comment
				then CONGRATULATE
				and SEND_10_EUROS_COUPON
		* */
		Facts facts = new Facts(factsMap);
		DecisionResults resultsTable = runner.apply(facts, new File(fileName));

		List<String> events = resultsTable.getResponses();
		assertEquals("This rule has a description", resultsTable.getResults().get(0).getDescription());
		assertEquals("This rule has a description too", resultsTable.getResults().get(2).getDescription());
		assertEquals(2, events.size());
		assertLinesMatch(events, Arrays.asList("CONGRATULATE", "SEND_10_EUROS_COUPON"));
	}

	@Test
	void runFromFileNameAndAnnotedFacts() {
		String fileName = "src/test/resources/decisions.jdl";

		/*
			Matching rule:
			when amount is 0.1
				and disconnected not false
				then LOG_NOT_DISCONNECTED
				and LOG_CONNECTED
		 */
		Facts facts = new Facts(this);

		DecisionResults resultsTable = runner.apply(facts, new File(fileName));

		List<String> events = resultsTable.getResponses();
		assertEquals("This rule has a description", resultsTable.getResults().get(0).getDescription());
		assertEquals("This rule has a description too", resultsTable.getResults().get(2).getDescription());
		assertEquals(3, events.size());
		assertLinesMatch(events, Arrays.asList("LOG_NOT_DISCONNECTED", "LOG_CONNECTED", "HOUSTON_GOT_PROBLEM"));
	}

	@Test
	void runFromInputStream() throws FileNotFoundException {
		String fileName = "src/test/resources/decisions.jdl";
		File file = new File(fileName);
		FileInputStream inputStream = new FileInputStream(file);
		/*
			Matching rule:
			when amount is 0.1
				and disconnected not false
				then LOG_NOT_DISCONNECTED
				and LOG_CONNECTED
		 */
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put("debt", "big");
		factsMap.put("life_expectance", "short");
		factsMap.put("numberOfPartners", 1);
		Facts facts = new Facts(factsMap);
		DecisionResults resultsTable = runner.apply(facts, new File(fileName));

		List<String> events = resultsTable.getResponses();
		assertEquals("This rule has a description", resultsTable.getResults().get(0).getDescription());
		assertEquals("This rule has a description too", resultsTable.getResults().get(2).getDescription());
		assertEquals(3, events.size());
		assertLinesMatch(events, Arrays.asList("SEND_COUPON", "SUGGEST_THERAPY", "CALL_THE_POLICE"));
	}

	@Test
	void runFromString() {
		Facts facts = new Facts(this);
		DecisionResults resultsTable = runner.apply(facts, "\"description.\"\n\n\n\n\nwhen connected is true and credit > 1234.56 then CONGRATULATE /*multilinecommentBut123132RQRQInline!!!~\\*/and SEND_10_EUROS_COUPON//Comment!@#@%!");

		List<String> events = resultsTable.getResponses();
		assertEquals(1, resultsTable.getResults().size());
		assertEquals("description.", resultsTable.getResults().get(0).getDescription());
		assertEquals(2, events.size());
		assertLinesMatch(events, Arrays.asList("CONGRATULATE", "SEND_10_EUROS_COUPON"));
	}

	@Test
	void runFromStrings() {
		Facts facts = new Facts(this);
		DecisionResults resultsTable = runner.apply(facts, "\"description.\"\n\n\n\n\nwhen connected is true and credit > 1234.56 then CONGRATULATE /*multilinecommentBut123132RQRQInline!!!~\\*/and SEND_10_EUROS_COUPON//Comment!@#@%!",
				"when alive == true and dead not true then EVENT_NAME");

		List<String> events = resultsTable.getResponses();
		assertEquals(2, resultsTable.getResults().size());
		assertEquals("description.", resultsTable.getResults().get(0).getDescription());
		assertEquals(2, events.size());
		assertLinesMatch(events, Arrays.asList("CONGRATULATE", "SEND_10_EUROS_COUPON"));
	}
}
