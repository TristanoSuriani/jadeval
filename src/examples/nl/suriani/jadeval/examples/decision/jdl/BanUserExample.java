package nl.suriani.jadeval.examples.decision.jdl;

import nl.suriani.jadeval.annotation.Fact;
import nl.suriani.jadeval.execution.JadevalExecutor;
import nl.suriani.jadeval.JadevalLoader;
import nl.suriani.jadeval.models.JadevalModel;
import nl.suriani.jadeval.execution.decision.DecisionResults;

import java.util.HashMap;
import java.util.Map;

class BanUserExample {
	public static void main(String[] args) {
		BanUserContext banUserContext = new BanUserContext(Status.REPORTED, 3, 5);

		String input = "\"Ban user if warned at least 3 times and reported more than 2 times\"" +
		"when status is REPORTED and numberOfWarnings >= 3 and timesReported >= 2 " +
				"then BAN_USER";

		JadevalModel model = new JadevalLoader().load(input);
		DecisionResults results = new JadevalExecutor(model).decision().apply(banUserContext);

		System.out.println(results.getResponses().get(0));
		// it prints BAN_USER
	}

	public static class BanUserContext {
		@Fact
		private Status status;

		@Fact
		private int numberOfWarnings;

		@Fact
		private int timesReported;

		public BanUserContext(Status status, int numberOfWarnings, int timesReported) {
			this.status = status;
			this.numberOfWarnings = numberOfWarnings;
			this.timesReported = timesReported;
		}

		public Status getStatus() {
			return status;
		}

		public int getNumberOfWarnings() {
			return numberOfWarnings;
		}

		public int getTimesReported() {
			return timesReported;
		}
	}

	public enum Status {
		DISABLED,
		ENABLED,
		REPORTED,
		WARNED,
		BANNED
	}
}
