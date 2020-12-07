package controllers;

import play.data.validation.Constraints;

public class FilterData {

    public String getFilterspecies() {
        return filterspecies;
    }

    public void setFilterspecies(String filterspecies) {
        this.filterspecies = filterspecies;
    }

    private String filterspecies;

    public FilterData() {
    }

}
