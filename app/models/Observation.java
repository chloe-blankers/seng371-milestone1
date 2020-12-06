package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Observation {
    //public Whale whale;
    public String date;
    public String time;
    public String location;

    public Observation( String date, String time, String location) {
        //this.whale = whale;
        this.date = date;
        this.time = time;
        this.location = location;
    }

}
