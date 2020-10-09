package nl.suriani.jadeval.examples.decision.jdl;

import nl.suriani.jadeval.JadevalExecutor;
import nl.suriani.jadeval.JadevalLoader;
import nl.suriani.jadeval.JadevalModel;
import nl.suriani.jadeval.decision.DecisionResults;
import nl.suriani.jadeval.common.annotation.Fact;

import java.io.File;
import java.math.BigDecimal;

class CloseAccountExample {
	public static void main(String[] args) {
		Person person = new Person();
		person.setAge(19);
		person.setFirstname("Piet");
		person.setLastname("de Haan");

		Account account = new Account();
		account.setOwner(person);
		account.setAmount(BigDecimal.valueOf(1234.56));
		account.setCanBeClosed(true);
		account.setDescription("custom");

		File file = new File("src/examples/nl/suriani/jadeval/examples/decision/jdl/close_account.jdl");
		JadevalModel model = new JadevalLoader().load(file);
		DecisionResults results = new JadevalExecutor(model).applyDecisions(person, account);

		results.getResponses().forEach(System.out::println);

		/* It prints:
			CLOSE_ACCOUNT
			SEND_CONFIRMATION_LETTER
			DEFAULT_DESCRIPTION
		 */
	}

	public static class Account {
		private Person owner;

		@Fact
		private BigDecimal amount;

		@Fact
		private boolean canBeClosed;

		@Fact(qualifier = "customStuff")
		private String description;

		public Person getOwner() {
			return owner;
		}

		public void setOwner(Person owner) {
			this.owner = owner;
		}

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		public boolean isCanBeClosed() {
			return canBeClosed;
		}

		public void setCanBeClosed(boolean canBeClosed) {
			this.canBeClosed = canBeClosed;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	public static class Person {
		private String firstname;

		private String lastname;

		@Fact
		private int age;

		public String getFirstname() {
			return firstname;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}

		public String getLastname() {
			return lastname;
		}

		public void setLastname(String lastname) {
			this.lastname = lastname;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}
}
