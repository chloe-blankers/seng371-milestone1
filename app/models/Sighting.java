package models;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Sighting {
    String whaleIDList;
    String location;
    String date;
    String time;


    /**
     *   Sighting object is different than the Observation object in that it only
     *   stores a comma-seperated String The ID's of the Whales instead of a List of the
     *   Whales like Observation. Sighting Object works well with the Play Framework forms.
     *   Sightings contain a comma-seperated String The ID's of the Whales in the Sighting
     *   Ex. 5,7,2,6,3,6
     *   The ID's of the Whales are found on the Whales page of the App.
     *   The whaleIDList is submitted on the form on the Observation page of the app
     *
     *
     *    @param whaleIDList  A comma-seperated String The ID's of the Whales in the Sighting
     *                        Ex. 5,7,2,6,3,6
     *                        The ID's of the Whales are found on the Whales page of the App.
     *                        The whaleIDList is submitted on the form on the Observation page of the app
     *    @param location     The location of the sighting
     *    @param date         The date of the sighting
     *    @param time         The time of the sighting
     *    @return  - None
     */
    public Sighting(String whaleIDList, String location, String date, String time) {
        this.whaleIDList = whaleIDList;
        this.location = location;
        this.date = date;
        this.time = time;
    }

    public String toString(){
        return "{ \"location\":\"" + location + "\", \"date\":" + date + ", \"time\":\"" + time + " },";
    }
}
