package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Observation {
    public ArrayList<Whale> whales;
    public String date;
    public String time;
    public String location;

    public Observation(ArrayList<Whale> whales, String date, String time, String location) {
        this.whales = whales;
        this.date = date;
        this.time = time;
        this.location = location;
    }

}
