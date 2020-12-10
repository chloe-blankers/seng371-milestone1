package models;

import controllers.ObservationData;
import controllers.WhaleData;
import play.data.DynamicForm;
import play.data.Form;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Code based off
 * https://github.com/playframework/play-java-forms-example
 *
 * Form processing tutorials.
 * https://playframework.com/documentation/latest/JavaForms
 * https://adrianhurt.github.io/play-bootstrap/
 */
public class Observation {
    public long id;
    public ArrayList<Whale> whales;
    public String date;
    public String time;
    public String location;

    private static final AtomicLong atomicLong = new AtomicLong(0L);

    public Observation(ArrayList<Whale> whales, String date, String time, String location) {
        this.whales = whales;
        this.date = date;
        this.time = time;
        this.location = location;
        this.id = atomicLong.getAndAdd(1);
    }

    //copy constructor
    public Observation(Observation other){
        if(other != null){
            this.whales = new ArrayList<Whale>(other.whales);
            this.date = other.date;
            this.time = other.time;
            this.location = other.location;
        }
    }

}
