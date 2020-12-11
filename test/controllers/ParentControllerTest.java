package controllers;

import com.google.common.collect.ImmutableMap;
import models.Whale;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import static org.junit.Assert.*;

import static play.test.Helpers.*;


import static play.test.Helpers.route;

public class ParentControllerTest extends WithApplication {

    @Override
    protected Application provideApplication(){
        return new GuiceApplicationBuilder().build();
    }

    //      GET     /                           controllers.ParentController.index
    @Test
    public void indexTest() {
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET").uri("/");
        Result result = route(app,request);
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
        assertTrue(contentAsString(result).contains("Welcome"));
    }

    //    POST    /Whales                    controllers.ParentController.createWhale(request: Request)
    @Test
    public void createWhaleTest() {
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method("POST")
                .bodyForm(ImmutableMap.of("species","Orca","weight","2200","gender","F"))
                .uri("/Whales");
        Result result = route(app,request);
        assertEquals(SEE_OTHER, result.status());
    }

    //    GET     /Whales                    controllers.ParentController.listWhales(request: Request)
    @Test
    public void listWhalesTest() {
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET").uri("/Whales");
        Result result = route(app,request);
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
    }

    //    GET     /Whales/getWhales          controllers.ParentController.getWhales(request: Request)
    @Test
    public void getWhalesTest() {
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET").uri("/Whales/getWhales");
        Result result = route(app,request);
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
    }



//    POST    /Whales/filter             controllers.ParentController.filterWhales(request: Request)
//    GET     /Whales/filter             controllers.ParentController.listFilterWhales(request: Request)
//    POST    /Whales/filter/removeFilter       controllers.ParentController.removeWhaleFilter()
    @Test
    public void whaleFilterTest() {
        Http.RequestBuilder request1 = Helpers.fakeRequest()
                .method("POST")
                .bodyForm(ImmutableMap.of("species","Orca"))
                .uri("/Whales/filter");
        Result result = route(app,request1);
        assertEquals(SEE_OTHER, result.status());
        //Add check for species types in Whales here??

        Http.RequestBuilder request2 = new Http.RequestBuilder().method("GET").uri("/Whales/filter");
        result = route(app,request2);
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());

        Http.RequestBuilder request3 = Helpers.fakeRequest()
                .method("POST")
                .uri("/Whales/filter");
        result = route(app,request3);
        assertEquals(SEE_OTHER, result.status());
    }



    //GET     /observations                    controllers.ParentController.listObservations(request: Request)
    @Test
    public void listObservationsTest(){
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET").uri("/observations");
        Result result = route(app,request);
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
    }

/*    //POST    /observations                    controllers.ParentController.createObservation(request: Request)
    @Test
    public void createObservationTest(){
        Whale w = new Whale("Orca",2222,"F");
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method("POST")
                .bodyForm(ImmutableMap.of("numWhales","0","weights","2222","gender","Male","species","Orca","location","Victoria, BC Canada"))
                .uri("/observations");
        Result result = route(app,request);
        assertEquals(SEE_OTHER, result.status());
    }
*/

//POST    /observations/filter             controllers.ParentController.filterObservations(request: Request)
//GET     /observations/filter             controllers.ParentController.listFilteredObservations(request: Request)
//POST    /observations/filter/removeFilter       controllers.ParentController.removeObservationFilter()
    @Test
    public void observationFilterTest() {
        Http.RequestBuilder request1 = Helpers.fakeRequest()
                .method("POST")
                .bodyForm(ImmutableMap.of("date","2020-12-01"))
                .uri("/observations/filter");
        Result result = route(app,request1);
        assertEquals(SEE_OTHER, result.status());
        //Add check for species types in Observations here??

        Http.RequestBuilder request2 = new Http.RequestBuilder().method("GET").uri("/observations/filter");
        result = route(app,request2);
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());

        Http.RequestBuilder request3 = Helpers.fakeRequest()
                .method("POST")
                .uri("/observations/filter");
        result = route(app,request3);
        assertEquals(SEE_OTHER, result.status());
    }

//GET     /Stats                      controllers.ParentController.stats
@Test
    public void statsTest(){
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET").uri("/Stats");
        Result result = route(app,request);
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
    }


}