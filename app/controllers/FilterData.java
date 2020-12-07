package controllers;

import play.data.validation.Constraints;

public class FilterData {

    public String getFilterspecies() {
        return filterspecies;
    }
    public void setFilterspecies(String filterspecies) {
        this.filterspecies = filterspecies;
    }
    public String getFiltergender() {
        return filtergender;
    }
    public void setFiltergender(String filtergender) {
        this.filtergender = filtergender;
    }

    private String filterspecies;
    private String filtergender;

    public FilterData() {
    }

}
