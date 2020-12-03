package controllers;

import models.Whale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

import static play.libs.Scala.asScala;

/**
 * An example of form processing.
 *
 * https://playframework.com/documentation/latest/JavaForms
 */
@Singleton
public class WhaleController extends Controller {

    private final Form<WhaleData> form;
    private MessagesApi messagesApi;
    private final List<Whale> Whales;

    private final Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Inject
    public WhaleController(FormFactory formFactory, MessagesApi messagesApi) {
        this.form = formFactory.form(WhaleData.class);
        this.messagesApi = messagesApi;
        this.Whales = com.google.common.collect.Lists.newArrayList(
                new Whale("Doug", "2019-02-13", "Beluga", 204, 11, "Male"),
                new Whale("Shelly", "2019-03-11", "Orca", 111, 225, "Female"),
                new Whale("Ben", "2018-05-17", "Blue", 301, 894, "Male")
        );
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result listWhales(Http.Request request) {
        return ok(views.html.listWhales.render(asScala(Whales), form, request, messagesApi.preferred(request)));
    }

    public Result createWhale(Http.Request request) {
        final Form<WhaleData> boundForm = form.bindFromRequest(request);

        if (boundForm.hasErrors()) {
            logger.error("errors = {}", boundForm.errors());
            logger.error("boundForm.errors().size():"+boundForm.errors().size());
            for(play.data.validation.ValidationError err: boundForm.errors()){
                logger.error(err.toString());
            }
            logger.error("boundForm.toString():"+boundForm.toString());
            return badRequest(views.html.listWhales.render(asScala(Whales), boundForm, request, messagesApi.preferred(request)));
        } else {
            WhaleData data = boundForm.get();
            Whales.add(new Whale(data.getName(), data.getDate(), data.getSpecies(), data.getSize(), data.getGridref(), data.getGender()));
            return redirect(routes.WhaleController.listWhales()).flashing("info", "Whale added!");
        }
    }
}
