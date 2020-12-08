package controllers;

import models.Observation;
import models.Whale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.*;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static play.libs.Scala.asScala;

/**
 * An example of form processing.
 *
 * https://playframework.com/documentation/latest/JavaForms
 * https://adrianhurt.github.io/play-bootstrap/
 */

public class ObservationController extends Controller {

    private final Form<ObservationData> form;
    private MessagesApi messagesApi;
    private final List<Observation> observations;
    private ArrayList<Whale> fakeWhales = new ArrayList<>();

    private final Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Inject
    public ObservationController(FormFactory formFactory, MessagesApi messagesApi) {
        this.form = formFactory.form(ObservationData.class);
        this.messagesApi = messagesApi;
        Whale w1 = new Whale( "Beluga", 204, "Male");
        Whale w2 = new Whale( "Orca", 111, "Female");
        Whale w3 = new Whale( "Blue", 301, "Male");
        ArrayList<Whale> whales = new ArrayList<>();
        whales.add(w1);
        whales.add(w2);
        whales.add(w3);
        this.observations = com.google.common.collect.Lists.newArrayList(
            new Observation(whales, LocalDate.now().toString(), "1pm", "Canada, BC, Victoria")
        );

        // TODO: Remove this when not needed anymore (when ListObservations can display the whales from the BD).
        this.fakeWhales.add(w1);
        this.fakeWhales.add(w2);
        this.fakeWhales.add(w3);
        this.fakeWhales.add( new Whale( "Beluga", 10000, "Male") );
        this.fakeWhales.add( new Whale( "Beluga", 11000, "Female") );
        this.fakeWhales.add( new Whale( "Orca", 10100, "Male") );
        this.fakeWhales.add( new Whale( "Sperm", 20000, "Male") );
        this.fakeWhales.add( new Whale( "Orca", 50000, "Female") );
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result listObservations(Http.Request request) {
        return ok(views.html.listObservations.render(asScala(observations), asScala(this.fakeWhales), form, request, messagesApi.preferred(request)));
    }

    public Result createObservation(Http.Request request) {
        final Form<ObservationData> boundForm = form.bindFromRequest(request);

        if (boundForm.hasErrors()) {
            logger.error("errors = {}", boundForm.errors());
            logger.error("boundForm.errors().size():"+boundForm.errors().size());
            for(play.data.validation.ValidationError err: boundForm.errors()){
                logger.error(err.toString());
            }
            logger.error("boundForm.toString():"+boundForm.toString());
            ArrayList<Whale> whales = new ArrayList<>();
            return badRequest(views.html.listObservations.render(asScala(observations), asScala(whales), boundForm, request, messagesApi.preferred(request)));
        } else {
            ObservationData data = boundForm.get();
            observations.add(new Observation(data.getWhales(), data.getDate(), data.getTime(), data.getLocation()));
            return redirect(routes.ObservationController.listObservations()).flashing("info", "Observation added!");
        }
    }

}

