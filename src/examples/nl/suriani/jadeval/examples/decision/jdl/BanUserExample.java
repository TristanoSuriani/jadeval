package nl.suriani.jadeval.examples.decision.jdl;

import nl.suriani.jadeval.execution.JadevalExecutor;
import nl.suriani.jadeval.JadevalLoader;
import nl.suriani.jadeval.models.JadevalModel;
import nl.suriani.jadeval.execution.decision.DecisionResults;

import java.util.HashMap;
import java.util.Map;

class BanUserExample {
	public static void main(String[] args) {
		Map<String, Object> factsMap = new HashMap<>();
		factsMap.put("status", Status.REPORTED);
		factsMap.put("numberOfWarnings", 3);
		factsMap.put("timesReported", 5);

		String input = "\"Ban user if warned at least 3 times and reported more than 2 times\"" +
		"when status is REPORTED and numberOfWarnings >= 3 and timesReported >= 2 " +
				"then BAN_USER";

		JadevalModel model = new JadevalLoader().load(input);
		DecisionResults results = new JadevalExecutor(model).applyDecisions(factsMap);

		System.out.println(results.getResponses().get(0));
		// it prints BAN_USER
	}

	public enum Status {
		DISABLED,
		ENABLED,
		REPORTED,
		WARNED,
		BANNED
	}
}
