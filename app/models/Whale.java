package models;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Presentation object used for displaying data in a template.
 *
 * Note that it's a good practice to keep the presentation DTO,
 * which are used for reads, distinct from the form processing DTO,
 * which are used for writes.
 */
public class Whale {
    public String species;
    public int weight;
    public String gender;
    public long id;

    private static final AtomicLong atomicLong = new AtomicLong(0L);



    public Whale( String species, int weight, String gender) {
        this.species = species;
        this.weight = weight;
        this.gender = gender;
        this.id = atomicLong.getAndAdd(1);
    }
}
