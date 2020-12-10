package controllers;



import com.fasterxml.jackson.databind.node.ObjectNode;
import db.DataStore;
import db.ResultData;
import models.Observation;
import models.Whale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
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
import views.html.listWhales;


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
    private Form<ObservationData> observationForm2;
    private final Form<WhaleData> whaleForm;
    private final Form<FilterData> whaleForm2;
    private List<Whale> FilteredWhales;
    private ArrayList<Whale> Whales;
    ArrayList<Whale> touristWhaleObs;
    private List<Observation> observations;
    private DataStore ds;

    private List<Observation> FilteredObservations;



    private final Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Inject
    public ParentController(FormFactory formFactory, MessagesApi messagesApi) throws IOException, SQLException {
        this.ds=new DataStore();
        ResultData rs = this.ds.setup(false);
        this.whaleForm = formFactory.form(WhaleData.class);
        this.whaleForm2 = formFactory.form(FilterData.class);
        this.observationForm = formFactory.form(ObservationData.class);
        this.messagesApi = messagesApi;
        this.Whales= (ArrayList<Whale>) rs.getWhaleList();
        this.touristWhaleObs = this.Whales;
        FilteredWhales = new ArrayList<>();
        this.observations = rs.getObservationList();
        this.insertDummyData();
    }
    /*
           Inserts dummy data into the application for testing and aesthetics,
           so the application opens with data in it.
     */
    public void insertDummyData() throws SQLException {
        Whale w1 = new Whale("Beluga", 204, "Male");
        Whale w2 = new Whale("Orca", 111, "Female");
        Whale w3 = new Whale("Blue", 301, "Male");
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
        return ok(views.html.listObservations.render(asScala(touristWhaleObs), asScala(Whales), asScala(observations), observationForm, whaleForm, whaleForm2, request, messagesApi.preferred(request)));
    }

    public Result createObservation(Http.Request request) throws SQLException {
        final Form<ObservationData> boundForm = observationForm.bindFromRequest(request);

        if (boundForm.hasErrors()) {
            logger.error("errors = {}", boundForm.errors());
            logger.error("boundForm.errors().size():"+boundForm.errors().size());
            for(play.data.validation.ValidationError err: boundForm.errors()){
                logger.error(err.toString());
            }
            logger.error("boundForm.toString():"+boundForm.toString());
            return badRequest(views.html.listObservations.render(asScala(touristWhaleObs), asScala(Whales), asScala(observations), observationForm, whaleForm, whaleForm2, request, messagesApi.preferred(request)));
        } else {
            ObservationData data = boundForm.get();
            ArrayList<Whale> whales = new ArrayList<>();
            int numWhales = data.getNumWhales();
            String weights = data.getWeights();
            String[] weigthsList = weights.split(",");
            for(int i = 0; i < numWhales; i++) {
                try {
                    Whale w = new Whale(data.getSpecies(), Integer.parseInt(weigthsList[i]), data.getGender());
                    whales.add(w);
                    touristWhaleObs.add(w);
                } catch (Exception e) {
                    Whale w = new Whale(data.getSpecies(), 0, data.getGender());
                    whales.add(w);
                    touristWhaleObs.add(w);
                }
            }
            Observation newOb = new Observation(whales, data.getDate(), data.getTime(), data.getLocation());
            observations.add(newOb);
            ds.addObservation(newOb);
            return redirect(routes.ParentController.listObservations()).flashing("info", "Observation added!");
        }
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result stats() {
        return ok(views.html.stats.render(Whales, observations));
    }


    public Result listWhales(Http.Request request) {
        return ok(views.html.listWhales.render(asScala(Whales), asScala(observations), observationForm, whaleForm, whaleForm2, request, messagesApi.preferred(request)));
    }

    public Result listFilterWhales(Http.Request request) {
        return ok(views.html.listWhales.render(asScala(FilteredWhales), asScala(observations), observationForm, whaleForm, whaleForm2, request, messagesApi.preferred(request)));
    }

    public Result getWhales(Http.Request request) {
        //Content negotiation
        if (request.accepts("text/html")) {
            return ok(views.html.listWhales.render(asScala(Whales), asScala(observations), observationForm, whaleForm, whaleForm2, request, messagesApi.preferred(request)));
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


    public Result createWhale(Http.Request request) throws IOException, SQLException {
        final Form<WhaleData> boundForm = whaleForm.bindFromRequest(request);
        if (boundForm.hasErrors()) {
            logger.error("errors = {}", boundForm.errors());
            logger.error("boundForm.errors().size():"+boundForm.errors().size());
            for(play.data.validation.ValidationError err: boundForm.errors()){
                logger.error(err.toString());
            }
            logger.error("boundForm.toString():"+boundForm.toString());
            return badRequest(views.html.listWhales.render(asScala(Whales), asScala(observations), observationForm, whaleForm, whaleForm2, request, messagesApi.preferred(request)));
        } else {
            WhaleData data = boundForm.get();
            System.out.println("data.getId():"+data.getId());
            Whale newWhale = new Whale(data.getSpecies(), data.getWeight(), data.getGender());
            Whales.add(newWhale);
            System.out.println("newWhale.id:"+newWhale.id);
            this.ds.addWhale(newWhale);
            List<Whale> allWhales = this.ds.getWhales();
            System.out.println("allWhales.size():"+allWhales.size());
            return redirect(routes.ParentController.listWhales()).flashing("info", "Whale added!");
        }
    }

    public Result filterWhales(Http.Request request) {
        System.out.println("hellloooo");
        final Form<FilterData> boundForm2 = whaleForm2.bindFromRequest(request);
        if (boundForm2.hasErrors()) {
            logger.error("errors = {}", boundForm2.errors());
            return badRequest(views.html.listWhales.render(asScala(FilteredWhales), asScala(observations), observationForm, whaleForm, whaleForm2, request, messagesApi.preferred(request)));
        } else {
            FilterData data = boundForm2.get();
            this.FilterWhales(data);
            return redirect(routes.ParentController.listFilterWhales()).flashing("info", "Whales Filtered");
        }
    }

    public void FilterWhales(FilterData data) {
        System.out.println("data.getFilterspecies():" + data.getFilterspecies());
        System.out.println("data.getFilterspecies().compareTo(\"None\"):" + data.getFilterspecies().compareTo("None"));
        if (data.getFilterspecies().compareTo("None") != 0) {
            FilteredWhales = Whales
                    .stream()
                    .filter(w -> w.species.trim().toLowerCase().startsWith(data.getFilterspecies().trim().toLowerCase()))
                    .collect(Collectors.toList());
        }
        System.out.println("data.getFiltergender():" + data.getFiltergender());
        System.out.println("data.getFiltergender().compareTo(\"None\"):" + data.getFiltergender().compareTo("None"));
        if (data.getFiltergender().compareTo("None") != 0) {
            FilteredWhales = FilteredWhales
                    .stream()
                    .filter(w -> w.gender.trim().toLowerCase().startsWith(data.getFiltergender().trim().toLowerCase()))
                    .collect(Collectors.toList());
        }
        System.out.println("data.getMaxweight():" + data.getMaxweight());
        if (data.getMaxweight() > 0) {
            System.out.println("if(data.getMaxweight()>0)");
            FilteredWhales = FilteredWhales
                    .stream()
                    .filter(w -> w.weight < (data.getMaxweight()))
                    .collect(Collectors.toList());
        }
        System.out.println("data.getMinweight():" + data.getMinweight());
        if (data.getMinweight() > 0) {
            System.out.println("if(data.getMinweight()>0)");
            FilteredWhales = FilteredWhales
                    .stream()
                    .filter(w -> w.weight > (data.getMinweight()))
                    .collect(Collectors.toList());
        }
    }

    public Result removeWhaleFilter(){
        System.out.println("hello whales");
        return redirect(routes.ParentController.listWhales()).flashing("info", "Whales Restored");
    }

    public void FilterObservationsList(ObservationData data){
        System.out.println("FilterObservationsList");
        if(data.getDate() != null) {
            FilteredObservations = observations
                    .stream()
                    .filter(w -> w.date.equals(data.getDate()))
                    .collect(Collectors.toList());
        }
    }

    public Result filterObservations(Http.Request request){
        System.out.println("filterObservations");
        final Form<ObservationData> boundForm = observationForm.bindFromRequest(request);

        if (boundForm.hasErrors()) {
            logger.error("errors = {}", boundForm.errors());
            return badRequest(views.html.listObservations.render(asScala(touristWhaleObs), asScala(Whales), asScala(observations), observationForm, whaleForm, whaleForm2, request, messagesApi.preferred(request)));
        } else {
            ObservationData data = boundForm.get();
            this.FilterObservationsList(data);
            return redirect(routes.ParentController.listFilteredObservations()).flashing("info", "Observations Filtered");
        }
    }

    public Result removeObservationFilter(){
        System.out.println("hello");
        return redirect(routes.ParentController.listObservations()).flashing("info", "Observations Restored");
    }

    public Result listFilteredObservations(Http.Request request) {
        return ok(views.html.listObservations.render(asScala(touristWhaleObs), asScala(Whales), asScala(FilteredObservations), observationForm, whaleForm, whaleForm2, request, messagesApi.preferred(request)));
    }

}
