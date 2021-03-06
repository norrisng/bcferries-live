package ca.norrisng.clarkark.webservice;

import ca.norrisng.clarkark.ferry.FerryRoute;
import ca.norrisng.clarkark.ferry.Sailing;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.util.ArrayList;

/**
 * Web scraper for BC Ferries' "Today's Departures and Arrivals" page
 * (http://orca.bcferries.com:8080/cc/marqui/actualDepartures.asp).
 */
public class ActualParser {

	static final boolean OFFLINE_TEST = false;

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

				// Edge case: last sailing of the day has departed from Tsawwassen and arrived
				if (currRow.equals("Data Currently Unavailable")) {

					// do nothing

				}

				// ignore "column title" rows
				else if (!currRow.contains("Status")) {

					// This row contains general route info (i.e. dep/arr ports, sailing time, day)
					if (currRow.contains("Sailing")) {

						// Strip out everything except for the departure port
						dep = currRow.replaceAll(" to.*","");

						// Strip out everything except for the arrival port
						arr = currRow.replaceAll(".* to ","");
						arr = arr.replaceAll(" Sailing.*","");

						// parse sailing time

						/* First, strip everything out so that we're left with "x hours y minutes" */
						String sailTimeRaw = currRow.replaceAll(".*Sailing time: ","");
						sailTimeRaw = sailTimeRaw.replaceAll(" \\w+day.*", "");

						// Initialize Duration to 0 - we'll add the hours/minutes as we parse them
						Duration sailDuration = Duration.ZERO;

						// Special case: sailing time is "variable"
						if (sailTimeRaw.equalsIgnoreCase("variable")) {
							sailDuration = null;
						}
						// Sailing is shorter than an hour (i.e. "x minutes")
						else if (!sailTimeRaw.contains("hour")) {
							sailTimeRaw = sailTimeRaw.replaceAll(" minutes", "");
							sailDuration = sailDuration.plusMinutes(Long.parseLong(sailTimeRaw));
						}
						// Sailing time only contains hours, not minutes (i.e. "x hours")
						else if (sailTimeRaw.contains("hour") && !sailTimeRaw.contains("minute")) {
							sailTimeRaw = sailTimeRaw.replaceAll(" hour(s)?","");
							sailDuration = sailDuration.plusHours(Long.parseLong(sailTimeRaw));
						}
						// Sailing time contains a mix of both hours and minutes (i.e. "x hours y minutes")
						else {
							String sailHoursRaw = sailTimeRaw.replaceAll(" hour(s)?.*","");
							String sailMinsRaw = sailTimeRaw.replaceAll(".*hour(s)? ","");
							sailMinsRaw = sailMinsRaw.replaceAll(" minutes","");

							sailDuration = sailDuration.plusHours(Long.parseLong(sailHoursRaw));
							sailDuration = sailDuration.plusMinutes(Long.parseLong(sailMinsRaw));
						}

						routes.add(new FerryRoute(dep, arr, sailDuration));
					}

					// This row contains info for a particular sailing (i.e. vessel, STD / ATD / ETA / status)
					else if (!currRow.contains("Sailing")) {

						// Strip out everything but the vessel name
						String shipName = currRow.replaceAll(" \\d{1,2}.*(A|P)M.*","");

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
										status += s + " ";
										break;
								}

								i++;
							}

							/*
								If the sailng is scheduled, but cancelled, currRow will contain "Cancelled" somewhere.
								In this case, anything parsed after the STD in currRow is likely garbage (and useless anyway),
								so we can just plug in the values below.
							*/
							if (currRow.contains("Cancelled")) {
								actualDep = "";
								estArr = "";
								status = "Cancelled";
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
