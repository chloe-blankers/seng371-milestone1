package models;

/**
 * Presentation object used for displaying data in a template.
 *
 * Note that it's a good practice to keep the presentation DTO,
 * which are used for reads, distinct from the form processing DTO,
 * which are used for writes.
 */
public class Whale {
    public String name;
    public String date;
    public String species;
    public int size;
    public int gridref;
    public String gender;


    public Whale(String name, String date, String species, int size, int gridref, String gender) {
        this.name = name;
        this.date = date;
        this.species = species;
        this.size = size;
        this.gridref = gridref;
        this.gender = gender;
    }
}
