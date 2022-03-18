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
import play.libs.Json;
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
    private Form<ObservationData> observationForm; // Input Form for observation searching on observation page
    private final Form<WhaleData> whaleForm; // The input Form on the Whales page
    private final Form<SightingData> sightingForm; // The input Form on the observation page
    private List<Whale> FilteredWhales; // Used when the User searches for a Whale
    private ArrayList<Whale> Whales; // Stores the Whales which shows up on the Whales page
    private List<Observation> observations; // The Observations
    private List<Observation> FilteredObservations; // Used when the user searches for an Observation

    private DataStore ds; // The DataStore class handles all access to the Database
    private final Logger logger = LoggerFactory.getLogger(getClass()); // Logger used to debug code
    private static final boolean dropDBTables = false; // Set this to true to Drop and Re-Create the database tables

    @Inject
    public ParentController(FormFactory formFactory, MessagesApi messagesApi) throws IOException, SQLException {
        FilteredWhales = new ArrayList<>();

        // Initialize the forms with the FormFactory
        this.whaleForm = formFactory.form(WhaleData.class);
        this.sightingForm = formFactory.form(SightingData.class);
        this.observationForm = formFactory.form(ObservationData.class);
        this.messagesApi = messagesApi;

        this.ds = new DataStore(); // Initialize the forms DataStore which handles connection to the H2 In-Memory
                                   // DB
        ResultData rs = this.ds.setup(dropDBTables); // Initialize tables and get the data already residing in DB
        this.observations = rs.getObservationList();
        this.Whales = (ArrayList<Whale>) rs.getWhaleList();

        this.insertDummyData(); // Insert dummy data into tables for aesthetics if there was no data in the DB
    }

    /**
     * Inserts dummy data into the application for testing and aesthetics,
     * so the application opens with data in it.
     *
     * @param
     * @return - None
     */
    public void insertDummyData() throws SQLException {
        Whale w1 = new Whale(0, "Beluga", 20400, "Male");
        Whale w2 = new Whale(1, "Orca", 11100, "Female");
        Whale w3 = new Whale(2, "Blue", 30100, "Male");
        add_dummy_whales(w1, w2, w3);
        add_dummy_observation(w1, w2, w3);
    }

    public void add_dummy_whales(Whale w1, Whale w2, Whale w3) throws SQLException {
        if (this.Whales.size() < 1) { // add dummy whales
            this.Whales = new ArrayList<>();
            Whales.add(w1);
            Whales.add(w2);
            Whales.add(w3);
            this.ds.addWhales(Whales);
        }
    }

    public void add_dummy_observation(Whale w1, Whale w2, Whale w3) throws SQLException {
        if (this.observations.size() < 1) { // add a dummy observation
            ArrayList<Whale> whales = new ArrayList<>();
            whales.add(w1);
            whales.add(w2);
            whales.add(w3);
            Observation ob = new Observation(whales, LocalDate.now().toString(), "1pm", "17.001, 15.334");
            this.observations = com.google.common.collect.Lists.newArrayList(ob);
            this.ds.addObservation(ob);
        }
    }

    /**
     *
     * Renders the observation list on the Observations page
     *
     * @param request
     * @return - Observation page rendering
     */
    public Result listObservations(Http.Request request) {
        return ok(views.html.listObservations.render(asScala(observations), sightingForm, observationForm, request,
                messagesApi.preferred(request)));
    }

    /**
     * REST request method that is called from the views to create a new
     * Observation.
     * Called with a input form that is bound from the request.
     *
     * @param request The Http.Request that the Form is bound from
     * @return - Result redirects to a method which will redirect to a view after
     *         the controller
     *         processes the new Observation to the Database
     */
    public Result createObservation(Http.Request request) throws SQLException {
        final Form<SightingData> boundForm = sightingForm.bindFromRequest(request);
        if (checkingForErrors(boundForm)) {
            return badRequest(views.html.listObservations.render(asScala(observations), sightingForm, observationForm,
                    request, messagesApi.preferred(request)));
        } else {
            SightingData data = boundForm.get();
            String whaleIDString = data.getWhaleIDList();
            String[] whaleIDList = whaleIDString.split(",");
            ArrayList<Whale> whales = addingWhalesToList(whaleIDList);
            Observation newOb = new Observation(whales, data.getDate(), data.getTime(), data.getLocation()); // create
                                                                                                             // new
                                                                                                             // observation
            observations.add(newOb);
            ds.addObservation(newOb);
            return redirect(routes.ParentController.listObservations()).flashing("info", "Observation added!");
        }
    }

    public boolean checkingForErrors(final Form<SightingData> boundForm) {
        if (boundForm.hasErrors()) {
            logger.error("errors = {}", boundForm.errors());
            logger.error("boundForm.errors().size():" + boundForm.errors().size());
            for (play.data.validation.ValidationError err : boundForm.errors()) {
                logger.error(err.toString());
            }
            logger.error("boundForm.toString():" + boundForm.toString());
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Whale> addingWhalesToList(String[] whaleIDList) {
        ArrayList<Whale> whalesList = new ArrayList<>();
        for (String s : whaleIDList) {
            int id = Integer.valueOf(s);
            Whale w = Whales.stream().filter(z -> z.id == id).collect(Collectors.toList()).get(0);
            whalesList.add(w);
        }
        return whalesList;
    }

    /*
     * Adam - Please Comment
     */
    public Result getObservations(Http.Request request) {
        if (request.accepts("text/html")) {
            return ok(views.html.listObservations.render(asScala(observations), sightingForm, observationForm, request,
                    messagesApi.preferred(request)));
        } else if (request.accepts("application/txt+json")) {
            ObjectNode result = observationResultAccepted();
            return ok(result);
        } else {
            ObjectNode result = observationResultRejected();
            return badRequest(result);
        }
    }

    public ObjectNode observationResultRejected() {
        ObjectNode resultOb = Json.newObject();
        resultOb.put("isSuccessful", false);
        resultOb.put("body", "MIME type not supported.");
        return resultOb;
    }

    public ObjectNode observationResultAccepted() {
        ObjectNode resultOb = Json.newObject();
        if (observations.size() > 0) {
            resultOb.put("isSuccessful", true);
            resultOb.putPOJO("body", observations);
        } else {
            resultOb.put("isSuccessful", true);
            resultOb.put("body", "No Observations in system");
        }
        return resultOb;

    }

    public Result getWhaleIDRange(Http.Request request) {
        if (request.accepts("application/txt+json")) {
            ObjectNode result = whaleIDResultAccepted();
            return ok(result);
        } else {
            ObjectNode result = whaleIDResultRejected();
            return badRequest(result);
        }
    }

    public ObjectNode whaleIDResultRejected() {
        ObjectNode resultWI = Json.newObject();
        resultWI.put("isSuccessful", true);
        resultWI.put("body", "No whales in system");
        return resultWI;
    }

    public ObjectNode whaleIDResultAccepted() {
        ObjectNode resultWI = Json.newObject();
        if (Whales.size() > 0) {
            long minWhaleID = 0;
            long maxWhaleID = Long.MAX_VALUE;
            for (Whale w : Whales) {
                if (w.id > minWhaleID)
                    minWhaleID = w.id;
                if (w.id < maxWhaleID)
                    maxWhaleID = w.id;
            }
            resultWI.put("isSuccessful", true);
            resultWI.putPOJO("minWhaleID", minWhaleID);
            resultWI.putPOJO("maxWhaleID", maxWhaleID);
        } else {
            resultWI.put("isSuccessful", true);
            resultWI.put("body", "No whales in system");
        }
        return resultWI;
    }

    /**
     * REST request method that redirects to the index.scala.html view.
     *
     * @param
     * @return - Result redirects to the stats.scala.html view
     */
    public Result index() {
        return ok(views.html.index.render());
    }

    /**
     * REST request method that redirects to the stats.scala.html view.
     * The default redirect for the Stats page.
     *
     * @param
     * @return - Result redirects to the stats.scala.html view
     */
    public Result stats() {
        return ok(views.html.stats.render(Whales, observations));
    }

    /**
     * REST request method that redirects to the listWhales.scala.html view
     * when the user has used the Search functionality on the Whales page and the
     * Whale
     * table gets filtered.
     *
     * @param request The Http.Request
     * @return - Result redirects to the listWhales.scala.html view
     */
    public Result listFilterWhales(Http.Request request) {
        return ok(views.html.listWhales.render(asScala(FilteredWhales), whaleForm, request,
                messagesApi.preferred(request)));
    }

    /**
     * REST request method that redirects to the listWhales.scala.html view.
     * The default redirect for the Whales page.
     *
     * @param request The Http.Request
     * @return - Result redirects to the listWhales.scala.html view
     */
    public Result listWhales(Http.Request request) {
        /*
         * if (request.accepts("text/html")) {
         * return ok(views.html.listWhales.render(asScala(Whales), whaleForm, request,
         * messagesApi.preferred(request)));
         * } else if (request.accepts("application/txt+json")) {
         * ObjectNode result = Json.newObject();
         * if (Whales.size() > 0) {
         * // convert Whales arraylist to json data
         * result.put("isSuccessful", true);
         * result.putPOJO("body", Whales);
         * // return json data
         * } else {
         * result.put("isSuccessful", true);
         * result.put("body", "No Whales in system");
         * }
         * return ok(result);
         * } else {
         * result.put("isSuccessful", false);
         * result.put("body", "MIME type not supported.");
         * return badRequest(result);
         * }
         */

        if (request.accepts("text/html")) {
            return ok(
                    views.html.listWhales.render(asScala(Whales), whaleForm, request, messagesApi.preferred(request)));
        } else {
            ObjectNode result = Json.newObject();
            if (request.accepts("application/txt+json")) {
                if (Whales.size() > 0) {
                    // convert Whales arraylist to json data
                    result.put("isSuccessful", true);
                    result.putPOJO("body", Whales);
                    // return json data
                } else {
                    result.put("isSuccessful", true);
                    result.put("body", "No Whales in system");
                }
                return ok(result);
            } else {
                result.put("isSuccessful", false);
                result.put("body", "MIME type not supported.");
                return badRequest(result);

            }
        }
    }

    /**
     * REST request method that is called from the views to create a new Whale.
     * Called with a input form that is binded from the request.
     *
     * @param request The Http.Request that the Form is binded from
     * @return - Result redirects to a method which will redirect to a view after
     *         the controller
     *         processes the new Whale to the Database
     */
    public Result createWhale(Http.Request request) throws IOException, SQLException {
        final Form<WhaleData> boundForm = whaleForm.bindFromRequest(request);
        if (checkingErrorInForm(boundForm)) {
            return badRequest(
                    views.html.listWhales.render(asScala(Whales), whaleForm, request, messagesApi.preferred(request)));
        } else {
            addWhaleFromDataForm(boundForm);
            return redirect(routes.ParentController.listWhales()).flashing("info", "Whale added!");
        }
    }

    public void addWhaleFromDataForm(final Form<WhaleData> boundForm) throws IOException, SQLException {
        WhaleData data = boundForm.get();
        Whale newWhale = new Whale(Whales.size(), data.getSpecies(), data.getWeight(), data.getGender());
        Whales.add(newWhale);
        this.ds.addWhale(newWhale);
        List<Whale> allWhales = this.ds.getWhales();
    }

    public boolean checkingErrorInForm(final Form<WhaleData> boundForm) throws IOException, SQLException {
        if (boundForm.hasErrors()) { // checks if the Form has errors
            logger.error("errors = {}", boundForm.errors());
            logger.error("boundForm.errors().size():" + boundForm.errors().size());
            for (play.data.validation.ValidationError err : boundForm.errors()) {
                logger.error(err.toString());
            }
            logger.error("boundForm.toString():" + boundForm.toString());
            return true;
        } else {
            return false;
        }
    }

    /**
     * REST request method which allows the search funcitonality required in the
     * specs.
     * When the user searches for a specific species this REST method filter the
     * data for that
     * Species. Could also be extended to filter on other fields too.
     *
     * @param request The Http.Request that the Form is binded from
     * @return - Result redirects to a method which will redirect to a view after
     *         filter happens
     */
    public Result filterWhales(Http.Request request) {
        final Form<WhaleData> boundForm = whaleForm.bindFromRequest(request);
        if (boundForm.hasErrors()) {
            logger.error("errors = {}", boundForm.errors());
            return badRequest(views.html.listWhales.render(asScala(FilteredWhales), whaleForm, request,
                    messagesApi.preferred(request)));
        } else {
            WhaleData data = boundForm.get();
            this.FilterWhalesList(data);
            return redirect(routes.ParentController.listFilterWhales()).flashing("info", "Whales Searched");
        }
    }

    /**
     *
     * Filters whale list based on species specified in the search species box
     *
     * @param data - supplies the form data indicating species for searching
     */
    public void FilterWhalesList(WhaleData data) {
        if (data.getSpecies() != null) {
            FilteredWhales = Whales
                    .stream()
                    .filter(w -> w.species.equals(data.getSpecies()))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Removes the filter from the users Search when the User hits the Show All
     * button in the UI.
     * The Whale list will be returned to its full size with all the Whales in the
     * UI
     * on the Whales page.
     *
     * @param
     * @return - Result redirects to a method which will redirect to a view after
     *         filter is removed
     */
    public Result removeWhaleFilter() {
        return redirect(routes.ParentController.listWhales()).flashing("info", "Whales Restored");
    }

    /**
     * Called from the REST method filterObservations and uses Java streams to
     * filter the
     * Observation List by the date that was extracted from the form that was bound.
     *
     * @param data The ObservationData that was extracted from the bound Form
     * @return - None
     */
    public void FilterObservationsList(ObservationData data) {
        if (data.getDate() != null) {
            FilteredObservations = observations
                    .stream()
                    .filter(w -> w.date.equals(data.getDate()))
                    .collect(Collectors.toList());
        }
    }

    /**
     * REST request method which allows the search funcitonality required in the
     * specs.
     * When the user searches for a Observation on a specific date of an
     * this REST method filter the data for that Observation for that date.
     * Could also be extended to filter on other fields too.
     *
     * @param request The Http.Request that the Form is binded from
     * @return - Result redirects to a method which will redirect to a view after
     *         filter happens
     */
    public Result filterObservations(Http.Request request) {
        final Form<ObservationData> boundForm = observationForm.bindFromRequest(request);

        if (boundForm.hasErrors()) {
            logger.error("errors = {}", boundForm.errors());
            return badRequest(views.html.listObservations.render(asScala(observations),
                    sightingForm, observationForm, request, messagesApi.preferred(request)));
        } else {
            ObservationData data = boundForm.get();
            this.FilterObservationsList(data);
            return redirect(routes.ParentController.listFilteredObservations()).flashing("info",
                    "Observations Searched");
        }
    }

    /**
     * Removes the filter from the users Search when the User hits the Show All
     * button in the UI.
     * The Observation list will be returned to its full size with all the
     * Observations in the UI
     * on the Observations page.
     *
     * @param
     * @return - Result redirects to a method which will redirect to a view after
     *         filter is removed
     */
    public Result removeObservationFilter() {
        return redirect(routes.ParentController.listObservations()).flashing("info", "Observations Restored");
    }

    /**
     * REST request method that redirects to the listObservations.scala.html view
     *
     * @param request The Http.Request
     * @return - Result redirects to the listObservations.scala.html view
     */
    public Result listFilteredObservations(Http.Request request) {
        return ok(views.html.listObservations.render(asScala(FilteredObservations),
                sightingForm, observationForm, request, messagesApi.preferred(request)));
    }

}