package controllers;

import models.Whale;
import play.data.validation.Constraints;

import javax.validation.Constraint;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ObservationData {

    //@Constraints.Required;
    private ArrayList<Whale> whales;

    //@Constraints.Required;
    private String location;

    //@Constraints.Required;
    private LocalDateTime time;

    public ObservationData() {
    }

    public ArrayList<Whale> getWhales() {
        return whales;
    }

    public void setWhales(ArrayList<Whale> whales) {
        this.whales = whales;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
