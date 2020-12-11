package controllers;

import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;

import static org.junit.Assert.*;
import static play.test.Helpers.*;



public class APITest extends WithApplication {

    @Override
    protected Application provideApplication(){
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void whalesGoodCallTest(){                                 //Test api call for get/whales
        RequestBuilder request = new RequestBuilder()
                .method("GET")
                .uri("/Whales")
                .header("Accept","application/txt+json");       //specify return type of json
        Result result = route(app,request);
        assertEquals(OK,result.status());                       //Assert correct Http response and mime type
        if (result.contentType().isPresent())
            assertEquals("application/json",result.contentType().get());
        else
            fail("No content type present in result");
    }

    @Test
    public void whalesBadMIMETest() {                           //Test for error checking on bad mime type
        RequestBuilder request = new RequestBuilder().method("GET")
                .uri("/Whales")
                .header("Accept","application/xml");            //Set an unsupported mime type
        Result result = route(app,request);
        assertEquals(BAD_REQUEST,result.status());
        assertNotEquals(OK,result.status());
    }

    @Test
    public void getObservationsTest(){                                              //Test for getObservations api call. Ensures json is returned
        Http.RequestBuilder request = new Http.RequestBuilder()                     //Build request
                .method("GET")
                .header("Accept","application/txt+json")                            //Add header application/txt+json to requst json result
                .uri("/observations/getObservations");
        Result result = route(app,request);
        assertEquals(OK, result.status());                                          //Assert Http response, mime type
        if (result.contentType().isPresent())
            assertEquals("application/json",result.contentType().get());
        else
            fail("No content type present in result");
        assertNotEquals(BAD_REQUEST,result.status());
    }
    //GET     /observations/getWhaleIdRange    controllers.ParentController.getWhaleIDRange(request: Request)
    @Test
    public void getWhaleIDRangeTest(){                              //Test for getWhaleIdRange api call. Ensures that json is returned
        Http.RequestBuilder request = new Http.RequestBuilder()     //Build request
                .method("GET")
                .uri("/observations/getWhaleIdRange")
                .header("Accept","application/txt+json");
        Result result = route(app,request);
        assertEquals(OK, result.status());                          //Assert Http response, mime type
        if (result.contentType().isPresent())
            assertEquals("application/json",result.contentType().get());
        else
            fail("No content type present in result");
    }
}
