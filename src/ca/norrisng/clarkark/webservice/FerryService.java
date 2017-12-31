package ca.norrisng.clarkark.webservice;

import ca.norrisng.clarkark.ferry.Sailing;

import java.util.ArrayList;
import java.util.Date;

/**
 * A parser that combines data from the "At a Glance" page (http://orca.bcferries.com:8080/cc/marqui/at-a-glance.asp)
 * and the "Today's Departures and Arrivals" page (http://orca.bcferries.com:8080/cc/marqui/actualDepartures.asp).
 */
public class FerryService {

	private ArrayList<Sailing> allSailings;

	/**
	 * Timestamp for the current data.
	 */
	private Date lastUpdated;

	public FerryService() {
		allSailings = new ArrayList<>();
	}

	/**
	 * Get all the sailings for the day.
	 * @return	All sailings for the day (scheduled or otherwise). The next 3 sailings on each route will
	 * 			include info on vessel loading (i.e. how full it is, in percent).
	 */
	public ArrayList<Sailing> getAllSailings() {
		return allSailings;
	}

	/**
	 * Get all sailings for a specific route
	 * @param dep		Departure port
	 * @param arr		Arrival port
	 * @return			All sailings for specified route
	 */
	public ArrayList<Sailing> getSailings(String dep, String arr) {

		ArrayList<Sailing> routeSailings = new ArrayList<>();

		for (Sailing s : allSailings) {
			if (s.getDep().equals(dep) && s.getArr().equals(arr))
				routeSailings.add(s);
		}

		return routeSailings;
	}

	/**
	 * Retrieve and update sailing data from BC Ferries' website.
	 */
	public void update() {

		lastUpdated = new Date();

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

	}

}
