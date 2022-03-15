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
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void getWhalesAPICallTest() {
        RequestBuilder request = new RequestBuilder()
                .method("GET")
                .uri("/Whales")
                .header("Accept", "application/txt+json"); // specify return type of json
        Result result = route(app, request);
        assertEquals(OK, result.status());
        if (result.contentType().isPresent())
            assertEquals("application/json", result.contentType().get());
        else
            fail("No content type present in result");
    }

    @Test
    public void getWhalesAPICallBadRequestTest() {
        RequestBuilder badRequest = new RequestBuilder().method("GET")
                .uri("/Whales")
                .header("Accept", "application/xml"); // Set an unsupported mime return type
        Result result = route(app, badRequest);
        assertEquals(BAD_REQUEST, result.status());
        assertNotEquals(OK, result.status());
    }

    @Test
    public void getObservationsAPICallTest() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method("GET")
                .header("Accept", "application/txt+json") // Specify return type of json
                .uri("/observations/getObservations");
        Result result = route(app, request);
        assertEquals(OK, result.status());
        if (result.contentType().isPresent())
            assertEquals("application/json", result.contentType().get());
        else
            fail("No content type present in result");
        assertNotEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void getWhaleIDRangeAPICallTest() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method("GET")
                .uri("/observations/getWhaleIdRange")
                .header("Accept", "application/txt+json");
        Result result = route(app, request);
        assertEquals(OK, result.status());
        if (result.contentType().isPresent())
            assertEquals("application/json", result.contentType().get());
        else
            fail("No content type present in result");
    }
}
