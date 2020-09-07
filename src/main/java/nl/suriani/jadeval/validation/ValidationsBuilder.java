package nl.suriani.jadeval.validation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ValidationsBuilder {

	public static <T> NewFromInputStream<T> newFromFile(File file) {
		try {
			return new NewFromInputStream(new FileInputStream(file));
		} catch (FileNotFoundException fileNotFoundException) {
			throw new IllegalArgumentException(fileNotFoundException);
		}
	}

	public static <T> NewFromInputStream<T> newFromInputStream(InputStream inputStream) {
		return new NewFromInputStream<>(inputStream);
	}

	public static <T> NewFromInputStream<T> newFromString(String input) {
		return new NewFromInputStream<>(new ByteArrayInputStream(input.getBytes()));
	}

	public static class NewFromInputStream<T> {
		private InputStream inputStream;

		private NewFromInputStream(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		public Validations build() {
			return new ValidationsDefinition().build(inputStream);
		}
	}
}
