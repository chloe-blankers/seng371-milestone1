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
import java.util.Locale;
import java.util.stream.Collectors;

import static play.libs.Scala.asScala;

/**
 * An example of form processing.
 *
 * https://playframework.com/documentation/latest/JavaForms
 * https://adrianhurt.github.io/play-bootstrap/
 */
@Singleton
public class WhaleController extends Controller {

    private final Form<WhaleData> form;
    private MessagesApi messagesApi;
    private final List<Whale> Whales;

    private final Form<FilterData> form2;
    private List<Whale> FilteredWhales;

    private final Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Inject
    public WhaleController(FormFactory formFactory, MessagesApi messagesApi) {
        this.form = formFactory.form(WhaleData.class);
        this.messagesApi = messagesApi;
        this.Whales = com.google.common.collect.Lists.newArrayList(
                new Whale( "Beluga", 204, "Male"),
                new Whale( "Orca", 111, "Female"),
                new Whale( "Blue", 301, "Male")
        );

        this.form2 = formFactory.form(FilterData.class);
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result listWhales(Http.Request request) {
        return ok(views.html.listWhales.render(asScala(Whales), form, form2, request, messagesApi.preferred(request)));
    }

    public Result listFilterWhales(Http.Request request) {
        return ok(views.html.listWhales.render(asScala(FilteredWhales), form, form2, request, messagesApi.preferred(request)));
    }

    public Result createWhale(Http.Request request) {
        System.out.println("ayyyyyy");
        final Form<WhaleData> boundForm = form.bindFromRequest(request);

        if (boundForm.hasErrors()) {
            logger.error("errors = {}", boundForm.errors());
            logger.error("boundForm.errors().size():"+boundForm.errors().size());
            for(play.data.validation.ValidationError err: boundForm.errors()){
                logger.error(err.toString());
            }
            logger.error("boundForm.toString():"+boundForm.toString());
            return badRequest(views.html.listWhales.render(asScala(Whales), boundForm, form2, request, messagesApi.preferred(request)));
        } else {
            WhaleData data = boundForm.get();
            Whales.add(new Whale(data.getSpecies(), data.getWeight(), data.getGender()));
            return redirect(routes.WhaleController.listWhales()).flashing("info", "Whale added!");
        }
    }

    public Result filterWhales(Http.Request request) {
        System.out.println("hellloooo");
        final Form<FilterData> boundForm2 = form2.bindFromRequest(request);

        if (boundForm2.hasErrors()) {
            logger.error("errors = {}", boundForm2.errors());
            return badRequest(views.html.listWhales.render(asScala(Whales), form, boundForm2, request, messagesApi.preferred(request)));
        } else {
            FilterData data = boundForm2.get();
            FilteredWhales = Whales
                        .stream()
                        .filter(w -> w.species.trim().toLowerCase().startsWith(data.getFilterspecies().trim().toLowerCase()))
                        .collect(Collectors.toList());
            FilteredWhales = FilteredWhales
                    .stream()
                    .filter(w -> w.gender.trim().toLowerCase().startsWith(data.getFiltergender().trim().toLowerCase()))
                    .collect(Collectors.toList());
            return redirect(routes.WhaleController.listFilterWhales()).flashing("info", "Whales Filtered");
        }
    }
}
