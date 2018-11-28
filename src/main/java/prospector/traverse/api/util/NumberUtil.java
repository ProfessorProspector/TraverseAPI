package prospector.traverse.api.util;

import java.security.InvalidParameterException;

public class NumberUtil {
	public static Integer parseHexString(String string) {
		if (string == null || string.isEmpty()) {
			return null;
		}
		if (string.contains("#")) {
			String[] splitString = string.split("#");
			if (splitString.length != 2) {
				throw new InvalidParameterException(string + " contains multiple # and is an invalid hex string");
			}
			string = splitString[1];
		}
		return Integer.parseInt(string, 16);
	}
}
