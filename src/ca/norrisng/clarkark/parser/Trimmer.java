package ca.norrisng.clarkark.parser;

public class Trimmer {

	/**
	 * Removes everything before one string, and everything after another.
	 * @param input
	 * @param startString
	 * @param endString
	 * @return
	 */
	protected static String trim(String input, String startString, String endString) {

		int tableEndLength = endString.length();
		String trimmedPage = input.substring(input.indexOf(startString), input.indexOf(endString) + tableEndLength);

		return trimmedPage;

	}

}
