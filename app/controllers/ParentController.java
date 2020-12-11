package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import db.DataStore;
import db.ResultData;
import models.Observation;
import models.Whale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static play.libs.Scala.asScala;

import play.libs.Json;

/**
 * Code based off
 * https://github.com/playframework/play-java-forms-example
 *
 * Form processing tutorials.
 * https://playframework.com/documentation/latest/JavaForms
 * https://adrianhurt.github.io/play-bootstrap/
 */
@Singleton
public class ParentController extends Controller {

    private MessagesApi messagesApi;
    private Form<ObservationData> observationForm;
    private final Form<WhaleData> whaleForm; //The input Form on the Whales page
    private final  Form<SightingData> sightingForm; //The input Form on the observation page
    private List<Whale> FilteredWhales; //Used when the User searches for a Whale.
    private ArrayList<Whale> Whales; //Stores the Whales which shows up on the Whales page
    ArrayList<Whale> touristWhaleObs; //Stores the Whales from the Observation page which is different than the Whales Page
    private List<Observation> observations; //The Observations
    private List<Observation> FilteredObservations; //Used when the user searches for an Observation

    private DataStore ds; //The DataStore class handles all access to the Database

    private final Logger logger = LoggerFactory.getLogger(getClass()); //Logger used to debug code
    private static final boolean dropDBTables = false; //Set this to true to Drop and Re-Create the database tables

    @Inject
    public ParentController(FormFactory formFactory, MessagesApi messagesApi) throws IOException, SQLException {
        this.touristWhaleObs = new ArrayList<>();
        FilteredWhales = new ArrayList<>();

        //Initialize the forms with the FormFactory
        this.whaleForm = formFactory.form(WhaleData.class);
        this.sightingForm = formFactory.form(SightingData.class);
        this.observationForm = formFactory.form(ObservationData.class);
        this.messagesApi = messagesApi;

        this.ds=new DataStore(); //Initialize the forms DataStore which handles connection to the H2 In-Memory DB
        ResultData rs = this.ds.setup(dropDBTables);  //Initialize tables and get the data already residing in DB
        this.observations = rs.getObservationList();
        this.Whales= (ArrayList<Whale>) rs.getWhaleList();
      
        this.insertDummyData(); //Insert dummy data into tables for aesthetics if there was no data in the DB
    }


    /**
     *       Inserts dummy data into the application for testing and aesthetics,
     *       so the application opens with data in it.
     *
     *    @param None
     *    @return  - None
     */
    public void insertDummyData() throws SQLException {
        Whale w1 = new Whale(0,"Beluga", 204, "Male");
        Whale w2 = new Whale(1, "Orca", 111, "Female");
        Whale w3 = new Whale(2, "Blue", 301, "Male");
        if(this.Whales.size()<1) { //add dummy whales
            this.Whales = new ArrayList<>();
            Whales.add(w1);
            Whales.add(w2);
            Whales.add(w3);
            this.touristWhaleObs = this.Whales;
            this.ds.addWhales(Whales);
        }
        if(this.observations.size()<1){ //add a dummy observation
            System.out.println("dummy");
            ArrayList<Whale> whales = new ArrayList<>();
            whales.add(w1);
            whales.add(w2);
            whales.add(w3);
            Observation ob = new Observation(whales, LocalDate.now().toString(), "1pm", "Canada, BC, Victoria");
            this.observations = com.google.common.collect.Lists.newArrayList(
                    ob
            );
            this.ds.addObservation(ob);
        }

    }

    public Result listObservations(Http.Request request) {
        return ok(views.html.listObservations.render(asScala(observations),
                sightingForm, observationForm, request, messagesApi.preferred(request)));
    }

    /**
     *    REST request method that is called from the views to create a new Observation.
     *    Called with a input form that is binded from the request.
     *
     *    @param request    The Http.Request that the Form is binded from
     *    @return  - Result redirects to a method which will redirect to a view after the controller
     *              processes the new Observation to the Database
     */
    public Result createObservation(Http.Request request) throws SQLException {
        final Form<SightingData> boundForm = sightingForm.bindFromRequest(request);
        if (boundForm.hasErrors()) {
            logger.error("errors = {}", boundForm.errors());
            logger.error("boundForm.errors().size():"+boundForm.errors().size());
            for(play.data.validation.ValidationError err: boundForm.errors()){
                logger.error(err.toString());
            }
            logger.error("boundForm.toString():"+boundForm.toString());
            return badRequest(views.html.listObservations.render(asScala(observations),
                    sightingForm, observationForm, request, messagesApi.preferred(request)));
        } else {
            SightingData data = boundForm.get();
            ArrayList<Whale> whales = new ArrayList<>();
            String whaleIDString = data.getWhaleIDList();
            String[] whaleIDList = whaleIDString.split(",");
            for(String s : whaleIDList) {
                int id = Integer.valueOf(s);
                Whale w = Whales.stream().filter(z -> z.id == id).collect(Collectors.toList()).get(0);
                touristWhaleObs.add(w);
                whales.add(w);
            }
            Observation newOb = new Observation(whales, data.getDate(), data.getTime(), data.getLocation());
            observations.add(newOb);
            ds.addObservation(newOb);
            return redirect(routes.ParentController.listObservations()).flashing("info", "Observation added!");


        }
    }

    /*
        Adam - Please Comment
    */
    public Result getObservations(Http.Request request) {
        //Content negotiation
        if (request.accepts("text/html")) {
            return ok(views.html.listObservations.render(asScala(observations),
                    sightingForm, observationForm, request, messagesApi.preferred(request)));
        }
        else {
            ObjectNode result = Json.newObject();
            if (request.accepts("application/txt+json")) {
                if (observations.size() > 0) {
                    //convert observations arraylist to json data
                    result.put("isSuccessful", true);
                    result.putPOJO("body", observations);
                    //return json data
                } else {
                    result.put("isSuccessful", true);
                    result.put("body", "No Observations in system");
                }
                return ok(result);
            }
            else{
                result.put("isSuccessful",false);
                result.put("body","MIME type not supported.");
                return badRequest(result);
            }
        }
    }

    /**
     *    REST request method that redirects to the index.scala.html view.
     *
     *    @param request    The Http.Request
     *    @return  - Result redirects to the stats.scala.html view
     */
    public Result index() {
        return ok(views.html.index.render());
    }


    /**
     *    REST request method that redirects to the stats.scala.html view.
     *    The default redirect for the Stats page.
     *
     *    @param request    The Http.Request
     *    @return  - Result redirects to the stats.scala.html view
     */
    public Result stats() {
        return ok(views.html.stats.render(Whales, observations));
    }


    /**
     *    REST request method that redirects to the listWhales.scala.html view.
     *    The default redirect for the Whales page.
     *
     *    @param request    The Http.Request
     *    @return  - Result redirects to the listWhales.scala.html view
     */
    public Result listWhales(Http.Request request) {
        return ok(views.html.listWhales.render(asScala(Whales), whaleForm, request, messagesApi.preferred(request)));
    }

    /**
     *    REST request method that redirects to the listWhales.scala.html view
     *    when the user has used the Search functionality on the Whales page and the Whale
     *    table gets filtered.
     *
     *    @param request    The Http.Request
     *    @return  - Result redirects to the listWhales.scala.html view
     */
    public Result listFilterWhales(Http.Request request) {
        return ok(views.html.listWhales.render(asScala(FilteredWhales), whaleForm, request, messagesApi.preferred(request)));
    }

    /*
            Adam - Please Comment
     */
    public Result getWhales(Http.Request request) {
        //Content negotiation
        if (request.accepts("text/html")) {
            return ok(views.html.listWhales.render(asScala(Whales), whaleForm, request, messagesApi.preferred(request)));
        }
        else {
            ObjectNode result = Json.newObject();
            if (request.accepts("application/txt+json")) {
                if (Whales.size() > 0) {
                    //convert Whales arraylist to json data
                    result.put("isSuccessful", true);
                    result.putPOJO("body", Whales);
                    //return json data
                } else {
                    result.put("isSuccessful", true);
                    result.put("body", "No Whales in system");
                }
                return ok(result);
            }
            else{
                result.put("isSuccessful",false);
                result.put("body","MIME type not supported.");
                return badRequest(result);
            }


        }
    }

    /**
     *    REST request method that is called from the views to create a new Whale.
     *    Called with a input form that is binded from the request.
     *
     *    @param request    The Http.Request that the Form is binded from
     *    @return  - Result redirects to a method which will redirect to a view after the controller
     *               processes the new Whale to the Database
     */
    public Result createWhale(Http.Request request) throws IOException, SQLException {
        final Form<WhaleData> boundForm = whaleForm.bindFromRequest(request); //binds the form from the Http.Request
        if (boundForm.hasErrors()) { //checks if the Form has errors
            logger.error("errors = {}", boundForm.errors());
            logger.error("boundForm.errors().size():"+boundForm.errors().size());
            for(play.data.validation.ValidationError err: boundForm.errors()){
                logger.error(err.toString());
            }
            logger.error("boundForm.toString():"+boundForm.toString());
            return badRequest(views.html.listWhales.render(asScala(Whales), whaleForm, request, messagesApi.preferred(request)));
        } else { //If the Form has no errors get the Data from the form and process the data
            WhaleData data = boundForm.get();
            System.out.println("data.getId():"+data.getId());
            //create new Whale with the Data from the Form
            Whale newWhale = new Whale(Whales.size(), data.getSpecies(), data.getWeight(), data.getGender());
            Whales.add(newWhale); //adds the new whale to the whale list
            System.out.println("newWhale.id:"+newWhale.id);
            this.ds.addWhale(newWhale); //adds the new whale to the database
            List<Whale> allWhales = this.ds.getWhales();
            System.out.println("allWhales.size():"+allWhales.size());
            //Redirect back to the listWhales method which will redirect back to the listWhales view.
            return redirect(routes.ParentController.listWhales()).flashing("info", "Whale added!");
        }
    }

    /**
     *    REST request method which allows the search funcitonality required in the specs.
     *    When the user searches for a specific species this REST method filter the data for that
     *    Species. Could also be extended to filter on other fields too.
     *
     *    @param request    The Http.Request that the Form is binded from
     *    @return  - Result redirects to a method which will redirect to a view after filter happens
     */
    public Result filterWhales(Http.Request request) {
        final Form<WhaleData> boundForm = whaleForm.bindFromRequest(request);
        if (boundForm.hasErrors()) {
            logger.error("errors = {}", boundForm.errors());
            return badRequest(views.html.listWhales.render(asScala(FilteredWhales), whaleForm, request, messagesApi.preferred(request)));
        } else {
            WhaleData data = boundForm.get();
            this.FilterWhales(data);
            return redirect(routes.ParentController.listFilterWhales()).flashing("info", "Whales Searched");
        }
    }

    public void FilterWhales(WhaleData data) {
        if(data.getSpecies() != null) {
            FilteredWhales = Whales
                    .stream()
                    .filter(w -> w.species.equals(data.getSpecies()))
                    .collect(Collectors.toList());
        }
    }

    /**
     *    Removes the filter from the users Search when the User hits the Clear button in the UI.
     *    The Whale list will be returned to its full size with all the Whales in the UI
     *    on the Whales page.
     *
     *    @param None
     *    @return  - Result redirects to a method which will redirect to a view after filter is removed
     */
    public Result removeWhaleFilter(){
        System.out.println("hello whales");
        return redirect(routes.ParentController.listWhales()).flashing("info", "Whales Restored");
    }

    /**
     *    Called from the REST method filterObservations and uses Java streams to filter the
     *    Observation List by the date that was extracted from the form that was binded.
     *
     *    @param data    The ObservationData that was extracted from the binded Form
     *    @return  - None
     */
    public void FilterObservationsList(ObservationData data){
        System.out.println("FilterObservationsList");
        if(data.getDate() != null) {
            FilteredObservations = observations
                    .stream()
                    .filter(w -> w.date.equals(data.getDate()))
                    .collect(Collectors.toList());
        }
    }

    /**
     *    REST request method which allows the search funcitonality required in the specs.
     *    When the user searches for a Observation on a specific date of an
     *    this REST method filter the data for that Observation for that date.
     *    Could also be extended to filter on other fields too.
     *
     *    @param request    The Http.Request that the Form is binded from
     *    @return  - Result redirects to a method which will redirect to a view after filter happens
     */
    public Result filterObservations(Http.Request request){
        System.out.println("filterObservations");
        final Form<ObservationData> boundForm = observationForm.bindFromRequest(request);

        if (boundForm.hasErrors()) {
            logger.error("errors = {}", boundForm.errors());
            return badRequest(views.html.listObservations.render(asScala(observations),
                    sightingForm, observationForm, request, messagesApi.preferred(request)));
        } else {
            ObservationData data = boundForm.get();
            this.FilterObservationsList(data);
            return redirect(routes.ParentController.listFilteredObservations()).flashing("info", "Observations Searched");
        }
    }

    /**
     *    Removes the filter from the users Search when the User hits the Clear button in the UI.
     *    The Observation list will be returned to its full size with all the Observations in the UI
     *    on the Observations page.
     *
     *    @param None
     *    @return  - Result redirects to a method which will redirect to a view after filter is removed
     */
    public Result removeObservationFilter(){
        System.out.println("hello");
        return redirect(routes.ParentController.listObservations()).flashing("info", "Observations Restored");
    }

    /**
     *    REST request method that redirects to the listObservations.scala.html view
     *
     *    @param request    The Http.Request
     *    @return  - Result redirects to the listObservations.scala.html view
     */
    public Result listFilteredObservations(Http.Request request) {
        return ok(views.html.listObservations.render(asScala(FilteredObservations),
                sightingForm, observationForm, request, messagesApi.preferred(request)));
    }

}
