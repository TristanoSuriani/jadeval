package nl.suriani.jadeval.examples.validation.jvl;

import nl.suriani.jadeval.execution.JadevalExecutor;
import nl.suriani.jadeval.JadevalLoader;
import nl.suriani.jadeval.models.JadevalModel;
import nl.suriani.jadeval.annotation.Fact;
import nl.suriani.jadeval.execution.validation.ValidationException;

import java.io.File;

class ValidateOrderExample {
	public static void main(String[] args) {
		File file = new File("src/examples/nl/suriani/jadeval/examples/validation/jvl/validate_order.jvl");
		JadevalModel model = new JadevalLoader().load(file);
		JadevalExecutor jadevalExecutor = new JadevalExecutor(model);

		Product product1 = new Product(10);
		Account account1 = new Account(false);
		Order order1 = new Order(2, product1, account1);

		jadevalExecutor.applyValidations(new OrderValidationContext(order1));
		// This validation succeeds.

		Product product2 = new Product(10);
		Account account2 = new Account(false);
		Order order2 = new Order(11, product2, account2);

		try {
			jadevalExecutor.applyValidations(new OrderValidationContext(order2));
		} catch (ValidationException validationException) {
			System.out.println(validationException.getMessage());
		}
		/* This validation fails and prints:
			Validation 'The amount of product must be in stock':
			false IS true (itemsInStockGreaterThanAmount)
		 */

		Product product3 = new Product(10);
		Account account3 = new Account(false);
		Order order3 = new Order(0, product3, account3);

		try {
			jadevalExecutor.applyValidations(new OrderValidationContext(order3));
		} catch (ValidationException validationException) {
			System.out.println(validationException.getMessage());
		}
		/* This validation fails and prints:
			Validation 'The amount of product must be in stock':
			0 GREATER_THAN_EQUALS 1.0 (amount)
		 */

		Product product4 = new Product(10);
		Account account4 = new Account(true);
		Order order4 = new Order(1, product3, account4);
		try {
			jadevalExecutor.applyValidations(new OrderValidationContext(order4));
		} catch (ValidationException validationException) {
			System.out.println(validationException.getMessage());
		}
		/* This validation fails and prints:
			Validation 'The order cannot be processed for customers with a blocked account':
			true IS false (blockedAccount)
		 */
	}

	static class Order {
		private int amount;
		private Product product;
		private Account account;

		// and other stuff

		public Order(int amount, Product product) {
			this.amount = amount;
			this.product = product;
		}

		public Order(int amount, Product product, Account account) {
			this.amount = amount;
			this.product = product;
			this.account = account;
		}

		public int getAmount() {
			return amount;
		}

		public Product getProduct() {
			return product;
		}

		public Account getAccount() {
			return account;
		}
	}

	static class Product {
		private int itemsInStock;

		// and other stuff

		public Product(int itemsInStock) {
			this.itemsInStock = itemsInStock;
		}

		public int getItemsInStock() {
			return itemsInStock;
		}
	}

	static class Account {
		private boolean blocked;

		// and other stuff

		public Account(boolean blocked) {
			this.blocked = blocked;
		}

		public boolean isBlocked() {
			return blocked;
		}
	}

	static class OrderValidationContext {
		@Fact
		private int amount;

		@Fact
		private boolean blockedAccount;

		@Fact
		private boolean itemsInStockGreaterThanAmount;

		public OrderValidationContext(Order order) {
			this.amount = order.getAmount();
			this.blockedAccount = order.getAccount().isBlocked();
			this.itemsInStockGreaterThanAmount = order.getProduct().getItemsInStock() >= order.getAmount();
		}
	}
}
