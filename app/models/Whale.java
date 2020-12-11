package models;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Code based off
 * https://github.com/playframework/play-java-forms-example
 *
 * Form processing tutorials.
 * https://playframework.com/documentation/latest/JavaForms
 * https://adrianhurt.github.io/play-bootstrap/
 */
public class Whale {
    public String species;
    public int weight;
    public String gender;
    public long id;

    private static final AtomicLong atomicLong = new AtomicLong(0L);

    /**
     *    This constructor takes in the ID of the Whale being created as a parameter.
     *    So the constructor should be called like 'new Whale(whaleList.size(), ....)'
     *
     *    @param species    The species of the whale
     *    @param weight     The weight of the whale
     *    @param gender     The gender of the whale
     *    @return  - None
     */
    public Whale(long id, String species, int weight, String gender) {
        this.species = species;
        this.weight = weight;
        this.gender = gender;
        this.id = id;
    }

    /**
     *    This constructor does not take in ID of the Whale being created as a parameter.
     *    The constructor uses AtomicLong atomicLong getAndAdd to increment a counter.
     *    AtomicLong getAndAdd is similar to Auto-Increment in SQL, except getAndAdd increments the
     *    counter with every instance Whale that is created, so only use this Constructor if the
     *    the code strictly creates Whale for one purpose, and doesn't create extra Observations
     *    that are not for a different purpose.
     *
     *    @param species    The species of the whale
     *    @param weight     The weight of the whale
     *    @param gender     The gender of the whale
     *    @return  - None
     */
    public Whale(String species, int weight, String gender) {
        this.species = species;
        this.weight = weight;
        this.gender = gender;
        this.id = atomicLong.getAndAdd(1);
    }

    public String toString(){
        return "{ \"species\":\"" + species + "\", \"weight\":" + weight + ", \"gender\":\"" + gender + "\", \"id\":" + id + " },";
    }
}
