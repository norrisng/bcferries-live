package ca.norrisng.clarkark.test;

import ca.norrisng.clarkark.ferry.Sailing;
import ca.norrisng.clarkark.webservice.FerryService;

import java.util.ArrayList;

public class Main {

	// This may be of future use: https://twitter.com/BowenFerry

	public static void main(String[] args) {
	// write your code here

        try {

			/* 	NOTE: 	to use the offline file for testing purposes,
			 *			change the OFFLINE_TEST value inside GlanceParser and ActualParser
			 */
			FerryService cp = new FerryService();
			cp.update();
			ArrayList<Sailing> detailedSailings = cp.getAllSailings();
			ArrayList<Sailing> all = cp.getAllSailings();

			/* Print a nicely-formatted table of all sailings */

			String output = "";

			System.out.printf("%-20s %-35s %-30s %-11s %-11s %-20s %-30s %-10s",
								"From", "To", "Ship", "Sched dep", "Actual dep", "Est / actual arr", "Status", "Loading");
			System.out.println("");

			for (Sailing s : detailedSailings) {
//
//				// dep -> arr, ship, schedDep, actualDep, arrival, status, loading
				System.out.printf("%-20s %-35s %-30s %-11s %-11s %-20s %-30s %-10s",
									s.getDep(), s.getArr(), s.getShipName(),
									s.getSchedDep(), s.getActualDep(), s.getArrival(),
									s.getStatus(), s.getLoading());
				System.out.println("");

			}

        } catch (Exception e) {
			e.printStackTrace();
        }

	}
}