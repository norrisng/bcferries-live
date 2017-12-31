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

//			System.out.println("All sailings, after combining results from both pages...");
//
//			String output = "";
//
//			for (Sailing s : detailedSailings) {
//
//				// dep -> arr, ship, schedDep, actualDep, arrival, status, loading
//
//				output += s.getDep() + " --> " + s.getArr() + " / " + s.getShipName() + " / " +
//							" Scheduled " + s.getSchedDep().toString() + ", Departed " + s.getActualDep().toString() + ", Arrive(d) " + s.getArrival().toString() +
//							" / Status: " + s.getStatus() + " / " + Integer.toString(s.getLoading()) + "% full";
//
//				System.out.println(output);
//			}


        } catch (Exception e) {
			e.printStackTrace();
        }

	}
}