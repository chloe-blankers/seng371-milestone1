package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
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
public class WhaleController extends Controller {

    private final Form<WhaleData> form;
    private MessagesApi messagesApi;
    private final Form<FilterData> form2;
    private List<Whale> FilteredWhales;
    private List<Whale> Whales;
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

        this.form2 = formFactory.form(FilterData.class);
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result stats() {
        return ok(views.html.stats.render(Whales));
    }


    public Result listWhales(Http.Request request) {
        return ok(views.html.listWhales.render(asScala(Whales), form, form2, request, messagesApi.preferred(request)));
    }

    public Result listFilterWhales(Http.Request request) {
        return ok(views.html.listWhales.render(asScala(FilteredWhales), form, form2, request, messagesApi.preferred(request)));
    }

    public Result getWhales(Http.Request request) {
        //Content negotiation
        if (request.accepts("text/html")) {
            return ok(views.html.listWhales.render(asScala(Whales), form, form2, request, messagesApi.preferred(request)));
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
                    result.put("isSuccessful", false);
                    result.put("body", "No Whales in system");
                }
            }
            else{
                    result.put("isSuccessful",false);
                    result.put("body","MIME type not supported.");
            }

            return ok(result);
        }
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
            return badRequest(views.html.listWhales.render(asScala(Whales), boundForm, form2, request, messagesApi.preferred(request)));
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

    public Result filterWhales(Http.Request request) {
        System.out.println("hellloooo");
        final Form<FilterData> boundForm2 = form2.bindFromRequest(request);
        if (boundForm2.hasErrors()) {
            logger.error("errors = {}", boundForm2.errors());
            return badRequest(views.html.listWhales.render(asScala(FilteredWhales), form, form2, request, messagesApi.preferred(request)));
        } else {
            FilterData data = boundForm2.get();
            this.FilterWhales(data);
            return redirect(routes.WhaleController.listFilterWhales()).flashing("info", "Whales Filtered");
        }
    }

    public void FilterWhales(FilterData data){
        System.out.println("data.getFilterspecies():"+data.getFilterspecies());
        System.out.println("data.getFilterspecies().compareTo(\"None\"):"+data.getFilterspecies().compareTo("None"));
        if(data.getFilterspecies().compareTo("None")!=0) {
            FilteredWhales = Whales
                    .stream()
                    .filter(w -> w.species.trim().toLowerCase().startsWith(data.getFilterspecies().trim().toLowerCase()))
                    .collect(Collectors.toList());
        }
        System.out.println("data.getFiltergender():"+data.getFiltergender());
        System.out.println("data.getFiltergender().compareTo(\"None\"):"+data.getFiltergender().compareTo("None"));
        if(data.getFiltergender().compareTo("None")!=0) {
            FilteredWhales = FilteredWhales
                    .stream()
                    .filter(w -> w.gender.trim().toLowerCase().startsWith(data.getFiltergender().trim().toLowerCase()))
                    .collect(Collectors.toList());
        }
        System.out.println("data.getMaxweight():"+data.getMaxweight());
        if(data.getMaxweight()>0){
            System.out.println("if(data.getMaxweight()>0)");
            FilteredWhales = FilteredWhales
                    .stream()
                    .filter(w -> w.weight<(data.getMaxweight()))
                    .collect(Collectors.toList());
        }
        System.out.println("data.getMinweight():"+data.getMinweight());
        if(data.getMinweight()>0) {
            System.out.println("if(data.getMinweight()>0)");
            FilteredWhales = FilteredWhales
                    .stream()
                    .filter(w -> w.weight > (data.getMinweight()))
                    .collect(Collectors.toList());
        }
    }

    public Result removeWhaleFilter(){
        System.out.println("hello whales");
        return redirect(routes.WhaleController.listWhales()).flashing("info", "Whales Restored");
    }
}
