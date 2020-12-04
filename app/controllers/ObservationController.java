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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static play.libs.Scala.asScala;

public class ObservationController extends Controller {

    private final Form<ObservationData> form;
    private MessagesApi messagesApi;
    private final List<Observation> observations;

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
            new Observation(whales, LocalDateTime.now(), "Canada, BC, Victoria")
        );
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result listWhales(Http.Request request) {
        return ok(views.html.listWhales.render(asScala(observations), form, request, messagesApi.preferred(request)));
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
            return badRequest(views.html.listWhales.render(asScala(Whales), boundForm, request, messagesApi.preferred(request)));
        } else {
            Observation data = boundForm.get();
            observations.add(new Observation(data.getWhales(), data.get(), data.getGender()));
            return redirect(routes.ObservationController.listWhales()).flashing("info", "Whale added!");
        }
    }

}

