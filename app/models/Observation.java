package models;

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

    /**
     *    This constructor takes in the ID of the Observation being created as a parameter.
     *    So the constructor should be called like 'new Observation(observationList.size(), ....)'
     *
     *    @param id    The ID of the Observation being created
     *    @param whales    The whales spotted in the observation
     *    @param date    The date the observation happened
     *    @param time    The time the observation happened
     *    @param location    The location the observation happened
     *    @return  - None
     */
    public Observation(long id, ArrayList<Whale> whales, String date, String time, String location) {
        this.whales = whales;
        this.date = date;
        this.time = time;
        this.location = location;
        this.id = id;
    }

    /**
     *    This constructor does not take in ID of the Observation being created as a parameter.
     *    The constructor uses AtomicLong atomicLong getAndAdd to increment a counter.
     *    AtomicLong getAndAdd is similar to Auto-Increment in SQL, except getAndAdd increments the
     *    counter with every instance Observation that is created, so only use this Constructor if the
     *    the code strictly creates Observation for one purpose, and doesn't create extra Observations
     *    that are not for a different purpose.
     *
     *    @param whales    The whales spotted in the observation
     *    @param date    The date the observation happened
     *    @param time    The time the observation happened
     *    @param location    The location the observation happened
     *    @return  - None
     */
    public Observation(ArrayList<Whale> whales, String date, String time, String location) {
        this.whales = whales;
        this.date = date;
        this.time = time;
        this.location = location;
        this.id = atomicLong.getAndAdd(1);
    }
}
