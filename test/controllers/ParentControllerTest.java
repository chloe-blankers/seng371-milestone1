package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import static play.test.Helpers.route;

import java.beans.Transient;

/**
 * Code based off
 * https://github.com/playframework/play-samples/blob/2.8.x/play-java-rest-api-example/
 */

public class ParentControllerTest extends WithApplication {

    private JsonNode contentAsJson(Result result) {
        final String responseBody = contentAsString(result);
        return Json.parse(responseBody);
    }

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void getIndexTest() {
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET").uri("/"); // Build request for index()
                                                                                        // route
        Result result = route(app, request);
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
    }

    /*
     * Test that we can successfully create a whale and that it will appear in the
     * system
     */
    @Test
    public void createWhaleTest() {

        // Get number of whales currently in system
        Http.RequestBuilder apiRequest = new Http.RequestBuilder().method("GET").uri("/Whales").header("Accept",
                "application/txt+json");
        Result result = route(app, apiRequest);
        JsonNode whales = contentAsJson(result).get("body"); // convert json api response to the first whale's id
        int prev_size = whales.size();

        // Add a new whale to system
        Http.RequestBuilder createWhalerequest = Helpers.fakeRequest()
                .method("POST")
                .bodyForm(ImmutableMap.of("species", "Orca", "weight", "2200", "gender", "Female"))
                .uri("/Whales");
        result = route(app, createWhalerequest);
        assertEquals(SEE_OTHER, result.status()); // Assert HTTP return is correct

        // Get new list of whales in system
        result = route(app, apiRequest);
        whales = contentAsJson(result).get("body"); // convert json api response to the first whale's id
        int new_size = whales.size();
        JsonNode newWhale = whales.get(whales.size() - 1);

        // Assert number of whales in system has increased by 1, and that the most
        // recent addition is the one we added
        assertEquals(new_size - prev_size, 1);
        assertEquals(newWhale.get("species").toString(), "\"Orca\"");
        assertEquals(newWhale.get("weight").toString(), "2200");
        assertEquals(newWhale.get("gender").toString(), "\"Female\"");
    }

    @Test
    public void listWhalesTest() {
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET").uri("/Whales"); // Build request for
                                                                                              // createWhale() route
        Result result = route(app, request);
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
    }

    // POST /Whales/filter controllers.ParentController.filterWhales(request:
    // Request)
    // GET /Whales/filter controllers.ParentController.listFilterWhales(request:
    // Request)
    // POST /Whales/filter/removeFilter
    // controllers.ParentController.removeWhaleFilter()
    @Test
    public void whaleFilterTest() { // Test whale filter process. Add whale filter, get filtered list, remove filter
        Http.RequestBuilder request1 = Helpers.fakeRequest()
                .method("POST")
                .bodyForm(ImmutableMap.of("species", "Orca")) // Create request to filter by Orca
                .uri("/Whales/filter");
        Result result = route(app, request1);
        assertEquals(SEE_OTHER, result.status()); // Assert filter route is successful

        Http.RequestBuilder request2 = new Http.RequestBuilder().method("GET").uri("/Whales/filter"); // Create request
                                                                                                      // for getting
                                                                                                      // filtered list
        result = route(app, request2);
        assertEquals(OK, result.status()); // Assert correct HTTP response, mime type, and charset
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());

        Http.RequestBuilder request3 = Helpers.fakeRequest() // Create request remove filters
                .method("POST")
                .uri("/Whales/filter/removeFilter");
        result = route(app, request3);
        assertEquals(SEE_OTHER, result.status()); // Assert correct HTTP response correct
    }

    // GET /observations controllers.ParentController.listObservations(request:
    // Request)
    @Test
    public void listObservationsTest() { // Test listObservations method
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET").uri("/observations"); // Build request
        Result result = route(app, request);
        assertEquals(OK, result.status()); // Assert Http response, mime type, and charset match
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
    }

    // POST /observations controllers.ParentController.createObservation(request:
    // Request)
    @Test
    public void createObservationTest() { // Test for creating an Observation
        // API call to get the id of a whale in whales table
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET").uri("/Whales").header("Accept",
                "application/txt+json");
        Result result = route(app, request);
        String id = contentAsJson(result).get("body").get(0).get("id").toString(); // convert json api response to the
                                                                                   // first whale's id

        request = Helpers.fakeRequest() // build request for new observation
                .method("POST")
                .bodyForm(ImmutableMap.of("whaleIDList", id, "location", "11.212,15.555", "date", "2020-12-10", "time",
                        "1pm"))
                .uri("/observations");
        result = route(app, request);
        assertEquals(SEE_OTHER, result.status()); // Assert correct Http response
    }

    @Test
    public void getObservationsAPICallTest() { // Test for getObservations route
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET").uri("/observations/getObservations"); // Build
                                                                                                                    // request
        Result result = route(app, request);
        assertEquals(OK, result.status()); // Assert Http response, mime type, and charset match
        if (result.contentType().isPresent() && result.charset().isPresent()) {
            assertEquals("text/html", result.contentType().get());
            assertEquals("utf-8", result.charset().get());
        } else {
            fail("Content type and/or charset not present in result");
        }
        assertNotEquals(BAD_REQUEST, result.status());
    }

    // GET /observations/getWhaleIdRange
    // controllers.ParentController.getWhaleIDRange(request: Request)
    @Test
    public void getWhaleIDRangeTest() { // Test for getWhaleIdRange route. Ensures that html is not returned
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET").uri("/observations/getWhaleIdRange")
                .header("Accept", "text/html"); // Build request
        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status()); // Assert Http response, mime type, and charset match
    }

    @Test
    public void filterObservationsTest() {
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method("POST")
                .bodyForm(ImmutableMap.of("date", "2020-12-01"))
                .uri("/observations/filter");
        Result result = route(app, request);
        assertEquals(SEE_OTHER, result.status());
    }

    @Test
    public void listFilteredObservationsTest() {
        /*
         * Http.RequestBuilder request = new
         * Http.RequestBuilder().method("GET").uri("/observations/filter");
         * Result result = route(app, request);
         * assertEquals(OK, result.status()); // Assert Http success
         * if (result.contentType().isPresent() && result.charset().isPresent()) {
         * assertEquals("text/html", result.contentType().get());
         * assertEquals("utf-8", result.charset().get());
         * } else {
         * fail("Content type and/or charset not present in result");
         * }
         */

        Http.RequestBuilder request2 = new Http.RequestBuilder().method("GET").uri("/observations/filter");
        Result result = route(app, request2);
        assertEquals(OK, result.status()); // Assert Http success
        if (result.contentType().isPresent() && result.charset().isPresent()) {
            assertEquals("text/html", result.contentType().get());
            assertEquals("utf-8", result.charset().get());
        } else {
            fail("Content type and/or charset not present in result");
        }
    }

    @Test
    public void removeObservationFilterTest() {
        Http.RequestBuilder request = Helpers.fakeRequest() // Create requets to remove filter
                .method("POST")
                .uri("/observations/filter/removeFilter");
        Result result = route(app, request);
        assertEquals(SEE_OTHER, result.status());
    }

    @Test
    public void getStatsTest() {
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET").uri("/Stats");
        Result result = route(app, request);
        assertEquals(OK, result.status());
        if (result.contentType().isPresent() && result.charset().isPresent()) {
            assertEquals("text/html", result.contentType().get());
            assertEquals("utf-8", result.charset().get());
        } else {
            fail("Content type and/or charset not present in result");
        }
    }
}
