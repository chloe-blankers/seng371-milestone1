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

    public int getMinweight() {
        return minweight;
    }
    public void setMinweight(int minweight) {
        this.minweight = minweight;
    }
    public int getMaxweight() {
        return maxweight;
    }
    public void setMaxweight(int maxweight) {
        this.maxweight = maxweight;
    }

    private String filterspecies;
    private String filtergender;
    private int minweight;
    private int maxweight;

    public FilterData() {
    }

}
