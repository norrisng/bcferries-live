package ca.norrisng.clarkark.parser;

import ca.norrisng.clarkark.ferry.FerryRoute;
import ca.norrisng.clarkark.ferry.Sailing;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Web scraper for BC Ferries' "Today's Departures and Arrivals" page
 * (http://orca.bcferries.com:8080/cc/marqui/actualDepartures.asp).
 */
public class ActualParser {

	static final boolean OFFLINE_TEST = true;

	/**
	 * Retrieves/scrapes all sailings (scheduled or otherwise) from BC Ferries' website.
	 * @return		All sailings for today
	 */
	public ArrayList<Sailing> parse() {

		String rawPage = "";

		if (OFFLINE_TEST) {
			rawPage = getTestFile();
			System.out.println("Using offline test file...");
		}
		else {

			try {
				rawPage = Jsoup.connect("http://orca.bcferries.com:8080/cc/marqui/actualDepartures.asp").get().html();
			}
			catch (IOException e) {
				System.err.println("File download error");
			}

		}

		// Trim HTML

		String tableStart = "<!--insert adlist.asp file in here-->";
		String tableEnd = "<!-- InstanceEndEditable -->";

		String trimmedPage = Trimmer.trim(rawPage, tableStart, tableEnd);

		Document doc = Jsoup.parse(trimmedPage);


		ArrayList<FerryRoute> routes = new ArrayList<FerryRoute>();
		ArrayList<Sailing> allSailings = new ArrayList<Sailing>();

		String dep = "";
		String arr = "";

		for (Element table : doc.select("table")) {

			for (Element row : table.select("tr")) {

				String currRow = row.text();

				// Collapse space between "ETA:" and time, if present
				currRow = currRow.replace("ETA: ", "ETA:");

				// For debugging purposes: show unparsed line
//				System.out.println("currRow: " + currRow);


				// ignore "column title" rows
				if (!currRow.contains("Status")) {

					if (currRow.contains("Sailing")) {

						// Strip out everything except for the departure port
						dep = currRow.replaceAll(" to.*","");

						// Strip out everything except for the arrival port
						arr = currRow.replaceAll(".* to ","");
						arr = arr.replaceAll(" Sailing.*","");

//						System.out.println("\t" + dep + " --> " + arr);
						routes.add(new FerryRoute(dep, arr));
					}

					else if (!currRow.contains("Sailing")) {

//						System.out.println("\t" + dep + " --> " + arr);
						// Strip out everything but the vessel name
						String shipName = currRow.replaceAll(" \\d{1,2}.*(A|P)M.*","");
//						System.out.print("Ship: " + shipName + ": ");

						// First, collapse the space separating the minutes and "AM"/"PM" so we can call split() without losing them
						String rawTimes = currRow.replaceAll(" AM","AM").replaceAll(" PM","PM");

						// Now we can break it apart into STD/ATD/(E)TA/statuses
						String[] times = rawTimes.replaceAll(shipName,"").split(" ");

						// Parse the sailing times and status
						int i = 0;
						String schedDep = "";
						String actualDep = "";
						String estArr = "";
						String status = "";

						// special case: the sailing hasn't departed yet, so it'll only contain the STD
						if (times.length == 1)
							schedDep = times[0];

						else {
							for (String s : times) {

								switch (i) {
									case 0:			// blank; dummy instruction
										status = "";
										break;
									case 1:        // STD
										schedDep = s;
										break;
									case 2:        // ATD
										actualDep = s;
										break;
									case 3: {   // (E)TA
										estArr = s;
										if (estArr.equals("..."))
											estArr = null;
										break;
									}
									default:    // status
										status += " " + s;
										break;
								}

								i++;
							}
						}
						// end parsing sailings times and status


						try {
							allSailings.add(new Sailing(dep, arr, shipName, status, schedDep, actualDep, estArr));
						}
						catch (ParseException e) {
							System.err.println("schedDep, actualDep, or estArr could not be parsed.");
							e.printStackTrace();
						}

					}

				}
			}

		}

		System.out.println("\nDetected sailings as follows...");
		for (Sailing s : allSailings) {
			String output = "";
			output += 	s.getDep() + " --> " + s.getArr() + ": " +
						s.getShipName() + ", Scheduled " + s.getSchedDep() + ", Departed " + s.getActualDep() + ", Arrival " + s.getArrival() +
						"\nStatus: " + s.getStatus() + "\n";
			System.out.println(output);
		}

		System.out.println("\nDetected routes as follows...");
		for (FerryRoute r : routes) {
			System.out.println(r.getDep() + " --> " + r.getArr());
		}

		return allSailings;

	}

	/**
	 * Retrieves the test HTML file (test_actual.html) located in the current directory
	 * @return Raw HTML in test.html
	 */
	private String getTestFile() {

		String rawPage = "";

		try {
			BufferedReader br = new BufferedReader(new FileReader("test_actual.html"));
			String line;

			while ((line = br.readLine()) != null) {
				rawPage += line;
			}

			if (br != null)
				br.close();
		}
		catch (IOException e) {
			System.err.println("test_actual.html does not exist in current directory!");
		}

		return rawPage;

	}

}
