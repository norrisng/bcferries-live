package ca.norrisng.clarkark.ferry;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Represents a one-directional ferry route.
 */
public class FerryRoute {

	/**
	 * The departure terminal.
	 */
	private String dep;

	/**
	 * The arrival terminal.
	 */
	private String arr;

	/**
	 * The day on which all of the route's sailings are scheduled to operate.
	 */
	private LocalDate date;

	/**
	 * Scheduled length of the route.
	 */
	private Duration length;

	/**
	 * Scheduled sailings for the route.
	 */
	private ArrayList<Sailing> sailings;

	/**
	 * Initializes a one-directional ferry route with no sailings.
	 * @param dep		Departure port
	 * @param arr		Arrival port
	 * @param date		Date of all sailings on route
	 * @param length	Scheduled sailing time
	 */
	public FerryRoute(String dep, String arr, LocalDate date, Duration length) {
		this.dep = dep;
		this.arr = arr;
		this.date = date;
		this.length = length;

		sailings = new ArrayList<Sailing>();
	}

	public FerryRoute(String dep, String arr, LocalDate date) {
		this.dep = dep;
		this.arr = arr;
		this.date = date;

		sailings = new ArrayList<Sailing>();
	}

	public FerryRoute(String dep, String arr) {
		this.dep = dep;
		this.arr = arr;

		sailings = new ArrayList<Sailing>();
	}

	public FerryRoute(String dep, String arr, Duration length) {
		this.dep = dep;
		this.arr = arr;
		this.length = length;

		sailings = new ArrayList<Sailing>();
	}

	public String getDep() {
		return dep;
	}

	public String getArr() {
		return arr;
	}

	public LocalDate getDate() {
		return date;
	}

	public Duration getLength() {
		return length;
	}

	public ArrayList<Sailing> getSailings() {
		return sailings;
	}

	/**
	 * Adds a sailing to the ferry route.
	 * @param sailing
	 */
	public void addSailing(Sailing sailing) {
		sailings.add(sailing);
	}

}
