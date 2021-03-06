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
    
Once `update()` has been called at least once, data can then be retrieved from the `FerryService` object:

    // Sailings for all of BC Ferries' major routes
    ArrayList<Sailing> allSailings = s.getAllSailngs();
    
    // All sailngs from Swartz Bay to Fulford Harbour
    ArrayList<Sailing> swartzToSaltspring = s.getSailings("Swartz Bay", "Fulford Harbour (Saltspring Is.");
    
    // All sailings operated by the Spirit of British Columbia
    ArrayList<Sailing> sobc = s.getShipSailings("Spirit of British Columbia");

## Sailings
    
A `Sailing` object represents a sailing (scheduled or otherwise), and includes the following attributes that can be accessed via the relevant getters:

* Departure/arrival port
* Ship name
* Short status (one of "Scheduled", "On time", "Delayed", "Cancelled", or "Arrived")
* Detailed status (as provided by BC Ferries)
* Scheduled departure time
* Actual departure time
* Arrival time (estimated or actual) 
* Loading (i.e. how full the sailing is, in percent)

### Important notes

#### Status

This library predicts whether future sailings are delayed. A scheduled sailing is marked as delayed if the ship was delayed on previous sailings. 

If a delay is predicted, this will be indicated in the detailed status as "Possible ongoing delay".

#### Departure and arrival times

If not yet available, `null` values are provided as appropriate.

#### Loading data

Loading data is only available for the next 3 departures on each route. Data is also not available if the sailing has already departed.

If loading data is not available, it will have a value of `0`.