# bcferries-live

*A Java library for scraping live BC Ferries sailing data*

`bcferries-live` is a Java library for scraping Sailing data from BC Ferries' website (specifically, the frames that reside on [Terminals at a Glance](http://www.bcferries.com/current_conditions/terminals.html) and [Today's Departures and Arrivals](http://www.bcferries.com/current_conditions/actualDepartures.html)).

This library is powered by [Jsoup](https://jsoup.org/).

## Usage

To obtain an ArrayList of all sailings (scheduled or otherwise):

    ActualParser ap = new ActualParser();
    ArrayList<Sailing> upcomingSailings = ap.parse();
    
A `Sailing` object represents a sailing (scheduled or otherwise), and a `FerryRoute` object represents a one-directional route (e.g. Tsawwassen to Swartz Bay, but not vice versa).

Searching for all Sailings that match a particular set of criteria (e.g. departure/arrival ports, route etc.) will be implemented in future releases.