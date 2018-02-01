package ca.norrisng.clarkark.ferry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;

/**
 * Represents a scheduled sailing.
 */
public class Sailing {

	/**
	 * Departure port.
	 */
	private String dep;

	/**
	 * Arrival port
	 */
	private String arr;

	/**
	 * Name of the ship. Does not include the "MV" prefix.
	 */
	private String shipName;

	/**
	 * Scheduled departure time.
	 */
	private LocalTime schedDep;

	/**
	 * Actual departure time.
	 */
	private LocalTime actualDep;

	/**
	 * Arrival time. If the ferry is en route, it is estimated; otherwise, it is the actual arrival time.
	 */
	private LocalTime arrival;

	/**
	 * Status of sailing. It is either "Scheduled", "On time", "Arrived", "Delayed", or "Cancelled".
	 */
	private String shortStatus;

	/**
	 * 	Full, detailed status of sailing. as provided by BC Ferries.
	 * 	May also include the reason for delay/cancellation, such as "heavy traffic volume",
	 * "cancelled due to high winds" etc.
	 */
	private String detailedStatus;

	/**
	 * How full the sailing is, in percent.
	 */
	private int loading;

	/**
	 * Creates a sailing based on the information available on the "At a Glance" page.
	 * @param dep			Departure port. Do not include the word "terminal" at the end.
	 * @param arr			Arrival port. Do not include the word "terminal" at the end.
	 * @param schedDep		Scheduled departure time
	 * @param loading		How full the sailing is, in percent
	 * @throws ParseException
	 */
	public Sailing(String dep, String arr, String schedDep, int loading) throws ParseException {
		this.dep = dep;
		this.arr = arr;
		this.schedDep = LocalTime.parse(getIso(schedDep));
		this.loading = loading;
	}

	/**
	 * Creates a sailing, without including information on car/oversize loadings.
	 * @param dep		Departure port. Do not include the word "terminal" at the end.
	 * @param arr		Arrival port. Do not include the word "terminal" at the end.
	 * @param shipName	Vessel name. Do not include the "MV" prefix.
	 * @param detailedStatus	Status ("Scheduled", "On Time", or other reason as provided by BC Ferries)
	 * @param schedDep	Scheduled departure time
	 * @param actualDep	Actual departure time
	 * @param arrival	Arrival time. May be estimated or actual. If not yet available, use null.
	 * @throws ParseException	Arrival time cannot be parsed
	 */
	public Sailing (String dep, String arr, String shipName, String detailedStatus, String schedDep, String actualDep, String arrival) throws ParseException {
		this.dep = dep;
		this.arr = arr;
		this.shipName = shipName;
		this.detailedStatus = detailedStatus;

		this.schedDep = LocalTime.parse(getIso(schedDep));

		if (!actualDep.equals(""))
			this.actualDep = LocalTime.parse(getIso(actualDep));
		else
			this.actualDep = null;

		// no arrival time yet
		if (arrival == null || arrival.equals("") || arrival.equals("..."))
			this.arrival = null;

		else {
			// strip the "ETA:" if present
			this.arrival = LocalTime.parse(getIso(arrival.replace("ETA:","")));
		}

		this.shortStatus = generateShortStatus();
	}

	/**
	 * Parses the raw status as provided by BC Ferries and generates the short status, based on
	 * the actual/scheduled departure and arrival times, in addition to the original raw status.
	 * This can only be called after departure/arrival times are set!
	 * @return			"Scheduled", "On time", "Arrived", "Delayed", or "Cancelled"
	 */
	private String generateShortStatus() {

		String output = "";

		// Not yet departed
		if (actualDep == null)
			output = "Scheduled";

		// Arrived
		else if(arrival != null && arrival.isBefore(LocalTime.now()))
			output = "Arrived";

		// Delayed
		//	i.e. when (schedDep + 10 mins) < actualDep
		else if (schedDep.plusMinutes(10).isBefore(actualDep))
			output = "Delayed";

		// Cancelled
		else if (detailedStatus.contains("cancelled"))
			output = "Cancelled";

		// On time
		else if (detailedStatus.contains("On Time")) {
			output = "On time";
		}

		return output;
	}

	/**
	 * Converts 12-hour time to ISO 8601-compliant 24-hour time.
	 * @param input		12-hour time; e.g. 1:30PM, 10:00AM etc.
	 * @return			ISO 8601-compliant 24-hour time; e.g. 02:30, 22:00 etc.
	 */
	private String getIso(String input) throws ParseException {

		SimpleDateFormat inFormat = new SimpleDateFormat("hh:mmaa");
		SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm");
		return outFormat.format(inFormat.parse(input));

	}

	/**
	 * Retrives the departure port of this sailing.
	 * @return		Name of departure port
	 */
	public String getDep() {
		return dep;
	}

	/**
	 * Retrieves the arrival port of this sailing.
	 * @return		Name of arrival port
	 */
	public String getArr() {
		return arr;
	}

	/**
	 * Retrieves the vessel name for this sailing.
	 * @return		Vessel name, without the "MV" prefix
	 */
	public String getShipName() {
		return shipName;
	}

	/**
	 * Retrieves the scheduled departure time for this sailing.
	 * @return		Scheduled departure time
	 */
	public LocalTime getSchedDep() {
		return schedDep;
	}

	/**
	 * Retrieves the actual departure time for this sailing.
	 * @return		Actual departure time. <code>null</code> if the sailing hasn't departed yet.
	 */
	public LocalTime getActualDep() {
		return actualDep;
	}

	/**
	 * Retrieves the arrival time for this sailing.
	 * @return		Arrival time. If it hasn't arrived yet, it is instead the estimated arrival time.
	 * 				<code>null</code> if the sailing hasn't departed yet.
	 */
	public LocalTime getArrival() {
		return arrival;
	}

	/**
	 * Retrives the status of the sailing.
	 * @return		"Scheduled", "On time", "Arrived", "Delayed", or "Cancelled"
	 */
	public String getShortStatus() {
		return shortStatus;
	}

	/**
	 * Retrieves the detailed status of the sailing, as provided by BC Ferries.
	 * @return		Detailed status of the sailing. Includes reason for delay/cancellation, if available.
	 */
	public String getDetailedStatus() {
		return detailedStatus;
	}

	/**
	 * Retrieves the loading of the current sailing. This includes both cars and oversized vehicles.
	 * Only available for the next 3 sailings.
	 * @return		How full the sailing is, in percent.
	 * 				If the sailing has already departed, or is not one of the next 3 departures,
	 * 				a value of <code>0</code> is returned.
	 */
	public int getLoading() {
		return loading;
	}

	/**
	 * Sets the loading of the current sailing.
	 * @param loading	How full the sailing is, in percent
	 */
	public void setLoading(int loading) {
		this.loading = loading;
	}

}
