package controllers;

/**
 * Code based off
 * https://github.com/playframework/play-java-forms-example
 *
 * Form processing tutorials.
 * https://playframework.com/documentation/latest/JavaForms
 * https://adrianhurt.github.io/play-bootstrap/
 *
 * Provides fields for whaleForm on Whale page. Enables whale creation and searching
 *
 */
public class WhaleData {

    private String species;

    private int weight;

    private String gender;

    private long id;

    public WhaleData() {
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int size) {
        this.weight = size;
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

    public long getId() {
        return id;
    }
}
