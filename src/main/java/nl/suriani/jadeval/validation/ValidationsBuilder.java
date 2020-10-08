package nl.suriani.jadeval.validation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ValidationsBuilder {

	public static <T> fromInputStream<T> fromFile(File file) {
		try {
			return new fromInputStream(new FileInputStream(file));
		} catch (FileNotFoundException fileNotFoundException) {
			throw new IllegalArgumentException(fileNotFoundException);
		}
	}

	public static <T> fromInputStream<T> fromInputStream(InputStream inputStream) {
		return new fromInputStream<>(inputStream);
	}

	public static <T> fromInputStream<T> newFromString(String input) {
		return new fromInputStream<>(new ByteArrayInputStream(input.getBytes()));
	}

	public static class fromInputStream<T> {
		private InputStream inputStream;

		private fromInputStream(InputStream inputStream) {
			this.inputStream = inputStream;
		}

		public Validations build() {
			return new ValidationsDefinition().build(inputStream);
		}
	}
}
