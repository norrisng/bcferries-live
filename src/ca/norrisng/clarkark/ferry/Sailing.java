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
	 * Status of sailing: "on time", "delayed", or "cancelled"
	 */
	private String status;

	/**
	 * How full the sailing is, in percent.
	 */
	private int loading;

	/**
	 * Number of car waits
	 */
	private int carWaits;

	/**
	 * Number of oversize waits.
	 */
	private int oversizeWaits;

	public Sailing(String dep, String arr, String shipName, String status, int loading, int carWaits, int oversizeWaits) {
		this.dep = dep;
		this.arr = arr;
		this.shipName = shipName;
		this.status = status;
		this.loading = loading;
		this.carWaits = carWaits;
		this.oversizeWaits = oversizeWaits;
	}

	/**
	 * Creates a sailing, without including information on car/oversize loadings.
	 * @param dep		Departure port. Do not include the word "terminal" at the end.
	 * @param arr		Arrival port. Do not include the word "terminal" at the end.
	 * @param shipName	Vessel name. Do not include the "MV" prefix.
	 * @param status	Status ("Scheduled", "On Time", or other reason as provided by BC Ferries)
	 * @param schedDep	Scheduled departure time
	 * @param actualDep	Actual departure time
	 * @param arrival	Arrival time. May be estimated or actual. If not yet available, use null.
	 * @throws ParseException	Arrival time cannot be parsed
	 */
	public Sailing (String dep, String arr, String shipName, String status, String schedDep, String actualDep, String arrival) throws ParseException {
		this.dep = dep;
		this.arr = arr;
		this.shipName = shipName;
		this.status = status;

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

		if (this.actualDep == null)
			this.status = "Scheduled";
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

	public String getDep() {
		return dep;
	}

	public String getArr() {
		return arr;
	}

	public String getShipName() {
		return shipName;
	}

	public LocalTime getSchedDep() {
		return schedDep;
	}

	public LocalTime getActualDep() {
		return actualDep;
	}

	public LocalTime getArrival() {
		return arrival;
	}

	public String getStatus() {
		return status;
	}

	public int getLoading() {
		return loading;
	}

	public int getCarWaits() {
		return carWaits;
	}

	public int getOversizeWaits() {
		return oversizeWaits;
	}
}
