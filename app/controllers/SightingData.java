package controllers;

import java.util.List;

public class SightingData {
    private String whaleIDList;

    //@Constraints.Required
    private String location;

    //@Constraints.Required
    private String date;

    //@Constraints.Required
    private String time;

    public String getWhaleIDList() {
        return whaleIDList;
    }
    public void setWhaleIDList(String whaleIDList) {
        this.whaleIDList = whaleIDList;
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
