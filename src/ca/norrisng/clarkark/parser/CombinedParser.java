package ca.norrisng.clarkark.parser;

import ca.norrisng.clarkark.ferry.Sailing;

import java.util.ArrayList;

/**
 * A parser that combines data from the "At a Glance" page (http://orca.bcferries.com:8080/cc/marqui/at-a-glance.asp)
 * and the "Today's Departures and Arrivals" page (http://orca.bcferries.com:8080/cc/marqui/actualDepartures.asp).
 */
public class CombinedParser {

	/**
	 * Parse data from both "At a Glance" and "Today's Departures and Arrivals", and combine the two together.
	 * @return	All sailings for the day (scheduled or otherwise). The next 3 sailings on each route will
	 * 			include info on vessel loading (i.e. how full it is, in percent).
	 */
	public ArrayList<Sailing> getAllSailings() {

		ArrayList<Sailing> allSailings = new ArrayList<>();
		ArrayList<Sailing> detailedSailings = new ArrayList<>();

		ActualParser ap = new ActualParser();
		allSailings = ap.parse();

		GlanceParser gp = new GlanceParser();
		detailedSailings = gp.parse();

		// Add loading info to the relevant sailings
		for (Sailing detailedSailing : detailedSailings) {

			for (Sailing a : allSailings) {

				// TODO: null pointer at getArr() (possibly resolved already?)
				if (detailedSailing.getDep().equals(a.getDep()) &&
						detailedSailing.getArr().equals(a.getArr()) &&
						detailedSailing.getSchedDep().equals(a.getSchedDep())) {
					a.setLoading(detailedSailing.getLoading());
				}

			}

		}

		return allSailings;
	}

}
