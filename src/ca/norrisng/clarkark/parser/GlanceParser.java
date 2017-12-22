package ca.norrisng.clarkark.parser;

import ca.norrisng.clarkark.ferry.Sailing;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * <p>
 * 	Web scraper for BC Ferries' "At a glance" page (http://orca.bcferries.com:8080/cc/marqui/at-a-glance.asp).
 * 	This page only contains scheduled times and ferry loadings.
 * </p>
 * <p>
 * 	At this time, the parser can only process the next 3 sailings.
 * </p>
 */
public class GlanceParser {

	static final boolean OFFLINE_TEST = false;

	/**
	 * Retrieves detailed info for the next 3 sailings.
	 * @return	The next 3 sailings
	 */
	public ArrayList<Sailing> parse() {

		String rawPage = "";

		if (OFFLINE_TEST) {
			rawPage = getTestFile();
			System.out.println("Using offline test file...");
		}
		else {

			try {
				rawPage = Jsoup.connect("http://orca.bcferries.com:8080/cc/marqui/at-a-glance.asp").get().html();
			}
			catch (IOException e) {
				System.err.println("File download error");
			}

		}

		/* trim HTML */

		String tableStart = "<!-- ============= start of the terminal ============= -->";
		String tableEnd = "<!-- ============= end of the terminals ============= -->";

//		int tableEndLength = tableEnd.length();
//		String trimmedPage = rawPage.substring(rawPage.indexOf(tableStart), rawPage.indexOf(tableEnd) + tableEndLength);

		String trimmedPage = Trimmer.trim(rawPage, tableStart, tableEnd);


		Document doc = Jsoup.parse(trimmedPage);


		ArrayList<Sailing> sailings = new ArrayList<Sailing>();

		for (Element table : doc.select("table")) {
			for (Element row : table.select("tr")) {
				Elements tds = row.select("td");
				if (tds.size() > 6) {

					String output = tds.get(0).text() + ":" + tds.get(1).text();

					String route = "";

					String dep = "";
					String arr = "";

					String scheduledSailings = "";
					String[] scheduledSailingsArray;

					if (!output.equals("Route:Next Sailings")) {
						System.out.println(output);

						route = output.split(":")[0];
						dep = route.split(" to ")[0];
						arr = route.split(" to ")[1];

						// Grab the sailings / loadings
						scheduledSailings = output.split("\\D:")[1];

						// Split the sailings / loadings into individual elements
						scheduledSailingsArray = scheduledSailings.split("\\s");

						/* parse jsoup'd data into ArrayList of Sailing objects */

						String depTime = "";
						int loading = 0;

						for (String str : scheduledSailingsArray) {

							// is str a time, loading, or "full" (useless)?
							if (str.matches(".*am") || str.matches(".*pm"))
								depTime = str;

							else if (str.matches(".*%"))
								loading = Integer.parseInt(str.replace("%",""));

							else {
								System.out.println("  adding sailing: " + dep + " --> " + arr + " at " + depTime + " (" + loading + "% full)");
								sailings.add(new Sailing(dep, arr, "n/a", "n/a", loading, 0, 0));
							}
						}

						/* end of parsing */
					}
					else
						System.out.println("\n");

				}
			}
		}


		return sailings;

	}

	/**
	 * Retrieves the test HTML file test_glance.html in the current directory
	 * @return Raw HTML in test.html
	 */
	private String getTestFile() {

		String rawPage = "";

		try {
			BufferedReader br = new BufferedReader(new FileReader("glance.html"));
			String line;

			while ((line = br.readLine()) != null) {
				rawPage += line;
			}

			if (br != null)
				br.close();
		}
		catch (IOException e) {

		}

		return rawPage;

	}

}