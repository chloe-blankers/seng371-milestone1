package controllers;

import play.data.validation.Constraints;

public class FilterData {

    public String getFilterspecies() {
        return filterspecies;
    }

    public void setFilterspecies(String filterspecies) {
        this.filterspecies = filterspecies;
    }

    @Constraints.Required
    private String filterspecies;

    public FilterData() {
    }

//    @Constraints.Min(0)
//    private int weight;
//
//    @Constraints.Required
//    private String gender;
//
//    @Constraints.Required
//    private long id;


}
