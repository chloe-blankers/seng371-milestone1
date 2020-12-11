package db;

import models.Observation;
import models.Whale;

import java.util.List;

/*
        ResultData
        The ResultData class is a wrapper to return data from
        the DataStore class.
*/
public class ResultData {
    private List<Whale> whaleList;
    private List<Observation> observationList;
    ResultData(List<Whale> whaleList, List<Observation> observationList){
        this.whaleList=whaleList;
        this.observationList=observationList;
    }
    public List<Whale> getWhaleList() {
        return whaleList;
    }
    public void setWhaleList(List<Whale> whaleList) {
        this.whaleList = whaleList;
    }
    public List<Observation> getObservationList() {
        return observationList;
    }
    public void setObservationList(List<Observation> observationList) {
        this.observationList = observationList;
    }
}
