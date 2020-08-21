package nl.suriani.jadeval.decision;

import nl.suriani.jadeval.decision.annotations.Fact;
import nl.suriani.jadeval.decision.internal.FactContainer;
import nl.suriani.jadeval.decision.internal.value.BooleanValue;
import nl.suriani.jadeval.decision.internal.value.NumericValue;
import nl.suriani.jadeval.decision.internal.value.TextValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DecisionsRunnerTest {

	private DecisionsRunner runner;

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
		runner = new DecisionsRunner();
		amount = 0.1;
		disconnected = true;
		connected = true;
		credit = 2000.01;
	}

	@Test
	void runFromFileName() {
		String fileName = "src/test/resources/decisions.txt";
		FactContainer<BooleanValue> factContainer1 = new FactContainer<>("connected", new BooleanValue(false));
		FactContainer<NumericValue> factContainer2 = new FactContainer<NumericValue>("credit", new NumericValue(2000));

		/*
			Matching rule:
			"This rule has a description"
			when connected is false
				and credit > 1234.56                // This is a comment
				then CONGRATULATE
				and SEND_10_EUROS_COUPON
		* */
		Facts facts = new Facts(Arrays.asList(factContainer1, factContainer2));
		DecisionsResultsTable resultsTable = runner.run(facts, new File(fileName));

		List<String> events = resultsTable.getEvents();
		assertEquals("This rule has a description", resultsTable.getResults().get(0).getDescription());
		assertEquals("This rule has a description too", resultsTable.getResults().get(2).getDescription());
		assertEquals(2, events.size());
		assertLinesMatch(events, Arrays.asList("CONGRATULATE", "SEND_10_EUROS_COUPON"));
	}

	@Test
	void runFromFileNameAndAnnotedFacts() {
		String fileName = "src/test/resources/decisions.txt";

		/*
			Matching rule:
			when amount is 0.1
				and disconnected not false
				then LOG_NOT_DISCONNECTED
				and LOG_CONNECTED
		 */
		Facts facts = Facts.fromObjects(this);

		DecisionsResultsTable resultsTable = runner.run(facts, new File(fileName));

		List<String> events = resultsTable.getEvents();
		assertEquals("This rule has a description", resultsTable.getResults().get(0).getDescription());
		assertEquals("This rule has a description too", resultsTable.getResults().get(2).getDescription());
		assertEquals(2, events.size());
		assertLinesMatch(events, Arrays.asList("LOG_NOT_DISCONNECTED", "LOG_CONNECTED"));
	}

	@Test
	void runFromInputStream() throws FileNotFoundException {
		String fileName = "src/test/resources/decisions.txt";
		File file = new File(fileName);
		FileInputStream inputStream = new FileInputStream(file);
		/*
			Matching rule:
			when amount is 0.1
				and disconnected not false
				then LOG_NOT_DISCONNECTED
				and LOG_CONNECTED
		 */
		FactContainer<BooleanValue> factContainer1 = new FactContainer<>("debt", new TextValue("big"));
		FactContainer<BooleanValue> factContainer2 = new FactContainer<>("life_expectance", new TextValue("short"));
		FactContainer<NumericValue> factContainer3 = new FactContainer<NumericValue>("numberOfPartners", new NumericValue(1));
		Facts facts = new Facts(Arrays.asList(factContainer1, factContainer2, factContainer3));
		DecisionsResultsTable resultsTable = runner.run(facts, new File(fileName));

		List<String> events = resultsTable.getEvents();
		assertEquals("This rule has a description", resultsTable.getResults().get(0).getDescription());
		assertEquals("This rule has a description too", resultsTable.getResults().get(2).getDescription());
		assertEquals(3, events.size());
		assertLinesMatch(events, Arrays.asList("SEND_COUPON", "SUGGEST_THERAPY", "CALL_THE_POLICE"));
	}

	@Test
	void runFromString() {
		Facts facts = Facts.fromObjects(this);
		DecisionsResultsTable resultsTable = runner.run(facts, "\"description.\"\n\n\n\n\nwhen connected is true and credit > 1234.56 then CONGRATULATE /*multilinecommentBut123132RQRQInline!!!~\\*/and SEND_10_EUROS_COUPON//Comment!@#@%!");

		List<String> events = resultsTable.getEvents();
		assertEquals(1, resultsTable.getResults().size());
		assertEquals("description.", resultsTable.getResults().get(0).getDescription());
		assertEquals(2, events.size());
		assertLinesMatch(events, Arrays.asList("CONGRATULATE", "SEND_10_EUROS_COUPON"));
	}

	@Test
	void runFromStrings() {
		Facts facts = Facts.fromObjects(this);
		DecisionsResultsTable resultsTable = runner.run(facts, "\"description.\"\n\n\n\n\nwhen connected is true and credit > 1234.56 then CONGRATULATE /*multilinecommentBut123132RQRQInline!!!~\\*/and SEND_10_EUROS_COUPON//Comment!@#@%!",
				"when alive == true and not dead then EVENT_NAME");

		List<String> events = resultsTable.getEvents();
		assertEquals(2, resultsTable.getResults().size());
		assertEquals("description.", resultsTable.getResults().get(0).getDescription());
		assertEquals(2, events.size());
		assertLinesMatch(events, Arrays.asList("CONGRATULATE", "SEND_10_EUROS_COUPON"));
	}
}
