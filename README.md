# bcferries-live

*A Java library for scraping live BC Ferries sailing data*

`bcferries-live` is a Java library for scraping sailing data from BC Ferries' website (specifically, the frames that reside on [Terminals at a Glance](http://www.bcferries.com/current_conditions/terminals.html) and [Today's Departures and Arrivals](http://www.bcferries.com/current_conditions/actualDepartures.html)).

This library is powered by [Jsoup](https://jsoup.org/).

## Usage

To obtain an ArrayList of all sailings (scheduled or otherwise):

    CombinedParser p = new CombinedParser();
    ArrayList<Sailing> allSailings = p.parse();

If available, the sailings returned will include loading data. However, if the sailings has already departed, the loading data will not be available.
    
A `Sailing` object represents a sailing (scheduled or otherwise), and includes the following attributes that can be accessed via the relevant getters:

* Departure/arrival port
* Ship name
* Status (e.g. "On Time", "Heavy Traffic" etc.)
* Scheduled departure time
* Actual departure time
* Arrival time (estimated or actual) 

If the sailing hasn't departured yet, the departure and arrival times will have a value of `null`.

A `FerryRoute` object represents a one-directional route (e.g. Tsawwassen to Swartz Bay, but not vice versa).

## Planned features

Searching for all Sailings that match a particular set of criteria (e.g. departure/arrival ports, route etc.) will be implemented in future releases.