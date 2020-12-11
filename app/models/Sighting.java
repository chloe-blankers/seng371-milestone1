package models;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Sighting {
    String whaleIDList;
    String location;
    String date;
    String time;



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
