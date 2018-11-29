package prospector.traverse.api.json;

public class BiomeLoadingException extends RuntimeException {
	public BiomeLoadingException() {
	}

	public BiomeLoadingException(String message) {
		super(message);
	}

	public BiomeLoadingException(String message, Throwable cause) {
		super(message, cause);
	}

	public BiomeLoadingException(Throwable cause) {
		super(cause);
	}

	public BiomeLoadingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
