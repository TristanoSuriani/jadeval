package nl.suriani.jadeval.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ValidationsBuilderTest {

	@Test
	public void test() {
		ValidationsBuilder<Train> validationsBuilder = new TrainValidationsBuilder();
		Validations<Train> validations = validationsBuilder.build();

		Assertions.assertThrows(AbnormallySmallTrainException.class, () -> validations.apply(new Train("Train", 1, 100)));
		Assertions.assertThrows(TrainIsCalledFrankValidationException.class, () -> validations.apply(new Train("Frank", 4, 199)));
		Assertions.assertThrows(DepressingIntercityTypeAException.class, () -> validations.apply(new Intercity("Damien", 4, 199, IntercityType.A)));
	}

	class TrainValidationsBuilder extends ValidationsBuilder<Train> {
		@Override
		public void compile() {
			validation("The train must have at least 100 chairs and 3 wagons")
					.when(always())
					.then(train -> train.chairs >= 100)
					.and(train -> train.wagons >= 3)
					.orElseThrow(AbnormallySmallTrainException::new);

			validation("A train cannot be called Frank")
					.when(always())
					.then(train -> !train.name.equals("Frank"))
					.orElseThrow(TrainIsCalledFrankValidationException::new);

			validation("An intercity of type A must have at least 5 wagons and 200 chairs")
					.when(itIsOfType(Intercity.class))
					.and(train -> ((Intercity) train).getType() == IntercityType.A)
					.then(train -> train.wagons >= 5)
					.and(train -> train.chairs >= 200)
					.orElseThrow(DepressingIntercityTypeAException::new);
		}
	}

	class Train {
		String name;
		int wagons;
		int chairs;

		public Train(String name, int wagons, int chairs) {
			this.name = name;
			this.wagons = wagons;
			this.chairs = chairs;
		}

		public Train() {
		}

		public String getName() {
			return this.name;
		}

		public int getWagons() {
			return this.wagons;
		}

		public int getChairs() {
			return this.chairs;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setWagons(int wagons) {
			this.wagons = wagons;
		}

		public void setChairs(int chairs) {
			this.chairs = chairs;
		}

		public boolean equals(final Object o) {
			if (o == this)
				return true;
			if (!(o instanceof Train))
				return false;
			final Train other = (Train) o;
			if (!other.canEqual((Object) this))
				return false;
			final Object this$name = this.name;
			final Object other$name = other.name;
			if (this$name == null ? other$name != null : !this$name.equals(other$name))
				return false;
			if (this.wagons != other.wagons)
				return false;
			if (this.chairs != other.chairs)
				return false;
			return true;
		}

		protected boolean canEqual(final Object other) {
			return other instanceof Train;
		}

		public int hashCode() {
			final int PRIME = 59;
			int result = 1;
			final Object $name = this.name;
			result = result * PRIME + ($name == null ? 43 : $name.hashCode());
			result = result * PRIME + this.wagons;
			result = result * PRIME + this.chairs;
			return result;
		}

		public String toString() {
			return "SimpleValidationsBuilderTest.Train(name=" + this.name + ", wagons=" + this.wagons + ", chairs=" + this.chairs + ")";
		}
	}

	class Intercity extends Train {
		private IntercityType type;

		public Intercity(String name, int wagons, int chairs, IntercityType type) {
			super(name, wagons, chairs);
			this.type = type;
		}

		public Intercity() {
		}

		public IntercityType getType() {
			return this.type;
		}

		public void setType(IntercityType type) {
			this.type = type;
		}

		public String toString() {
			return "SimpleValidationsBuilderTest.Intercity(type=" + this.type + ")";
		}
	}

	enum IntercityType {
		A, B
	}

	class AbnormallySmallTrainException extends ValidationException {

		public AbnormallySmallTrainException() {
			super("Aw come on! The train must have at least 100 chairs and 3 wagons");
		}
	}

	class TrainIsCalledFrankValidationException extends ValidationException {
		public TrainIsCalledFrankValidationException() {
			super("Really? Did you call a train 'Frank'???");
		}
	}

	class DepressingIntercityTypeAException extends ValidationException {

		public DepressingIntercityTypeAException() {
			super("What a depressing intercity type A!");
		}
	}

}
