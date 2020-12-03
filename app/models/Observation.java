package models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Observation {
    public ArrayList<Whale> whales;
    public LocalDateTime time;
    public String location;

    public Observation(ArrayList<Whale> whales, LocalDateTime time, String location) {
        this.whales = whales;
        this.time = time;
        this.location = location;
    }

}
