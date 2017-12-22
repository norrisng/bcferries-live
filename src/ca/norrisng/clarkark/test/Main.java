package ca.norrisng.clarkark.test;

import ca.norrisng.clarkark.ferry.Sailing;
import ca.norrisng.clarkark.parser.ActualParser;
import ca.norrisng.clarkark.parser.GlanceParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.FileReader;
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


        } catch (Exception e) {
			e.printStackTrace();
        }

	}
}