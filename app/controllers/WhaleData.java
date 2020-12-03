package controllers;

import play.data.validation.Constraints;

/**
 * A form processing DTO that maps to the Whale form.
 *
 * Using a class specifically for form binding reduces the chances
 * of a parameter tampering attack and makes code clearer, because
 * you can define constraints against the class.
 */
public class WhaleData {

    @Constraints.Required
    private String name;

    @Constraints.Required
    private String date;

    @Constraints.Required
    private String species;

    @Constraints.Min(0)
    private int size;

    @Constraints.Min(0)
    private int gridref;


    @Constraints.Required
    private String gender;

    public WhaleData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getGridref() {
        return gridref;
    }

    public void setGridref(int gridref) {
        this.gridref = gridref;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
