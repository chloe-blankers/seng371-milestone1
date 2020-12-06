package controllers;

import db.DataStore;
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
import java.util.List;

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
    private DataStore ds;

    private final Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Inject
    public WhaleController(FormFactory formFactory, MessagesApi messagesApi) {
        this.ds = new DataStore();
        this.form = formFactory.form(WhaleData.class);
        this.messagesApi = messagesApi;
        this.Whales = com.google.common.collect.Lists.newArrayList(
                new Whale( "Beluga", 204, "Male"),
                new Whale( "Orca", 111, "Female"),
                new Whale( "Blue", 301, "Male")
        );
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result listWhales(Http.Request request) {
        return ok(views.html.listWhales.render(asScala(Whales), form, request, messagesApi.preferred(request)));
    }

    public Result createWhale(Http.Request request) throws IOException, SQLException {
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
            System.out.println("data.getId():"+data.getId());
            Whale newWhale = new Whale(data.getSpecies(), data.getWeight(), data.getGender());
            Whales.add(newWhale);
            System.out.println("newWhale.id:"+newWhale.id);
            this.ds.addWhale(newWhale);
            List<Whale> allWhales = this.ds.getWhales();
            System.out.println("allWhales.size():"+allWhales.size());
            return redirect(routes.WhaleController.listWhales()).flashing("info", "Whale added!");
        }
    }
}
