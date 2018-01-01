package ca.norrisng.clarkark.webservice;

import ca.norrisng.clarkark.ferry.Sailing;

import java.util.ArrayList;
import java.util.Date;

/**
 * A parser that combines data from the "At a Glance" page (http://orca.bcferries.com:8080/cc/marqui/at-a-glance.asp)
 * and the "Today's Departures and Arrivals" page (http://orca.bcferries.com:8080/cc/marqui/actualDepartures.asp).
 */
public class FerryService {

	/**
	 * All sailing data for today.
	 */
	private ArrayList<Sailing> allSailings;

	/**
	 * Timestamp for the current data.
	 */
	private Date lastUpdated;

	/**
	 * Instantiates a new FerryService object.
	 */
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
	 * @return			All sailings for specified route. An empty ArrayList is returned if none are found.
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
	 * Get all sailings for a specific vessel
	 * @param shipName	Ship name. Do not include the "MV" prefix.
	 * @return			All sailings for the specified ship. An empty ArrayList is returned if none are found.
	 */
	public ArrayList<Sailing> getShipSailings(String shipName) {

		ArrayList<Sailing> vesselSailings = new ArrayList<>();

		for (Sailing s : allSailings) {
			if (s.getShipName().equals(shipName))
				vesselSailings.add(s);
		}

		return vesselSailings;

	}

	/**
	 * Retrieve and update sailing data from BC Ferries' website.
	 */
	public void update() {

		ArrayList<Sailing> detailedSailings = new ArrayList<>();

		ActualParser ap = new ActualParser();
		allSailings = ap.parse();

		lastUpdated = new Date();

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
