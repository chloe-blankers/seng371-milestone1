package models;

import controllers.WhaleData;
import play.data.DynamicForm;
import play.data.Form;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

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

}
