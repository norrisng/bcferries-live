# bcferries-live

*A Java library for scraping live BC Ferries sailing data*

`bcferries-live` is a Java library for scraping sailing data from BC Ferries' website (specifically, the frames that reside on [Terminals at a Glance](http://www.bcferries.com/current_conditions/terminals.html) and [Today's Departures and Arrivals](http://www.bcferries.com/current_conditions/actualDepartures.html)).

This library is powered by [Jsoup](https://jsoup.org/).

## Usage

*Disclaimer: this is a work in progress. Documented methods may change without prior notice!*

First, create a `FerryService` object:

    FerryService s = new FerryService();
    
To pull new data from BC Ferries' website:

    s.update();
    
To retrieve data from `FerryService`:

    // Sailings for all of BC Ferries' major routes
    ArrayList<Sailing> allSailings = s.getAllSailngs();
    
    // All sailngs from Tsawwassen to Swartz Bay
    ArrayList<Sailing> tsaSwb = s.getSailings("Tsawwassen", "Swartz Bay");
    
    // All sailings operated by the Spirit of British Columbia
    ArrayList<Sailing> sobc = s.getShipSailings("Spirit of British Columbia");

If available, the sailings returned will include loading data. However, if the sailing has already departed, the loading data will not be available.
    
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

Searching for all Sailings that match a particular set of criteria (e.g. departure/arrival ports, route etc.) will be implemented in the future.