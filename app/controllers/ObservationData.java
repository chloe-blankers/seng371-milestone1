package controllers;

import models.Whale;
import play.data.validation.Constraints;

import javax.validation.Constraint;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ObservationData {

    @Constraints.Required
    private Whale whale;

    @Constraints.Required
    private String location;

    @Constraints.Required
    private String date;

    @Constraints.Required
    private String time;

    public ObservationData() {
    }

    public Whale getWhale() {
        return whale;
    }

    public void setWhales(Whale whale) {
        this.whale = whale;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
