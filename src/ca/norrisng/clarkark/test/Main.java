package ca.norrisng.clarkark.test;

import ca.norrisng.clarkark.ferry.Sailing;
import ca.norrisng.clarkark.parser.ActualParser;
import ca.norrisng.clarkark.parser.CombinedParser;
import ca.norrisng.clarkark.parser.GlanceParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main {

	// This may be of future use: https://twitter.com/BowenFerry

	public static void main(String[] args) {
	// write your code here

		String rawPage = "";

        try {

			/* 	NOTE: 	to use the offline file for testing purposes,
			 *			change the OFFLINE_TEST value inside GlanceParser or ActualParser
			 */

			// Scrape vessel loading info
//			GlanceParser gp = new GlanceParser();
//			ArrayList<Sailing> sailings = gp.parse();

			// Scrape all sailings for today
			ActualParser ap = new ActualParser();
			ArrayList<Sailing> upcomingSailings = ap.parse();

//			CombinedParser cp = new CombinedParser();
//			ArrayList<Sailing> detailedSailings = cp.getAllSailings();
//
//			System.out.println("All sailings, after combining results from both pages...");
//
//			String output = "";
//
//			for (Sailing s : detailedSailings) {
//
//				// dep -> arr, ship, schedDep, actualDep, arrival, status, loading, carwaits, oversizeWaits
//
//				output += s.getDep() + "-->" + s.getArr() + " / " + s.getShipName() + " / " +
//							" Scheduled " + s.getSchedDep().toString() + ", Departed " + s.getActualDep().toString() + ", Arrive(d) " + s.getArrival().toString() +
//							" / Status: " + s.getStatus() + " / " + Integer.toString(s.getLoading()) + " full";
//
//				System.out.println(output);
//			}


        } catch (Exception e) {
			e.printStackTrace();
        }

	}
}