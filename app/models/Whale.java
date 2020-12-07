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
