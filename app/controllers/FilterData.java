package controllers;

import play.data.validation.Constraints;

public class FilterData {

    @Constraints.Required
    private String species;

    @Constraints.Min(0)
    private int weight;

    @Constraints.Required
    private String gender;

    @Constraints.Required
    private long id;

    public String getSpecies() {
        return species;
    }
}
