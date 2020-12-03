package models;

import java.util.concurrent.atomic.AtomicLong;

/**
 * An example of form processing.
 *
 * https://playframework.com/documentation/latest/JavaForms
 */
public class Whale {
    public String species;
    public int weight;
    public String gender;
    public long id;

    private static final AtomicLong atomicLong = new AtomicLong(0L);



    public Whale(String species, int weight, String gender) {
        this.species = species;
        this.weight = weight;
        this.gender = gender;
        this.id = atomicLong.getAndAdd(1);
    }
}
