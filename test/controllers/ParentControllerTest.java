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

    /* Test if we can successfully redirect to the index.scala.html view */
    @Test
    public void indexViewTest() {
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET").uri("/");
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

        // 1. Get number of whales currently in system
        Http.RequestBuilder apiRequest = new Http.RequestBuilder().method("GET").uri("/Whales").header("Accept",
                "application/txt+json");
        Result result = route(app, apiRequest);
        JsonNode whales = contentAsJson(result).get("body"); // convert json api response to the first whale's id
        int prev_size = whales.size();

        // 2. Add a new whale to system
        Http.RequestBuilder createWhalerequest = Helpers.fakeRequest()
                .method("POST")
                .bodyForm(ImmutableMap.of("species", "Orca", "weight", "2200", "gender", "Female"))
                .uri("/Whales");
        result = route(app, createWhalerequest);
        assertEquals(SEE_OTHER, result.status());

        // 3. Get new list of whales in system
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
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET").uri("/Whales");
        Result result = route(app, request);
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
    }

    /*
     * Test whale filter functions: create filter, get filtered list, remove filter
     */
    @Test
    public void whaleFilterTest() {

        // 1. create filter
        Http.RequestBuilder request1 = Helpers.fakeRequest()
                .method("POST")
                .bodyForm(ImmutableMap.of("species", "Orca"))
                .uri("/Whales/filter");
        Result result = route(app, request1);
        assertEquals(SEE_OTHER, result.status());

        // 2. get filtered list
        Http.RequestBuilder request2 = new Http.RequestBuilder().method("GET").uri("/Whales/filter");
        result = route(app, request2);
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());

        // 3. remove filter
        Http.RequestBuilder request3 = Helpers.fakeRequest()
                .method("POST")
                .uri("/Whales/filter/removeFilter");
        result = route(app, request3);
        assertEquals(SEE_OTHER, result.status());
    }

    @Test
    public void listObservationsTest() {
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET").uri("/observations");
        Result result = route(app, request);
        assertEquals(OK, result.status());
        assertEquals("text/html", result.contentType().get());
        assertEquals("utf-8", result.charset().get());
    }

    @Test
    public void createObservationTest() {
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET").uri("/Whales").header("Accept",
                "application/txt+json");
        Result result = route(app, request);
        // convert json api response to the first whales id
        String id = contentAsJson(result).get("body").get(0).get("id").toString();

        request = Helpers.fakeRequest() // build request for new observation
                .method("POST")
                .bodyForm(ImmutableMap.of("whaleIDList", id, "location", "11.212,15.555", "date", "2020-12-10", "time",
                        "1pm"))
                .uri("/observations");
        result = route(app, request);
        assertEquals(SEE_OTHER, result.status());
    }

    @Test
    public void getObservationsTest() {
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET").uri("/observations/getObservations");
        Result result = route(app, request);
        assertEquals(OK, result.status());
        if (result.contentType().isPresent() && result.charset().isPresent()) {
            assertEquals("text/html", result.contentType().get());
            assertEquals("utf-8", result.charset().get());
        } else {
            fail("Content type and/or charset not present in result");
        }
        assertNotEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void getWhaleIDRangeBadRequestTest() {
        Http.RequestBuilder request = new Http.RequestBuilder().method("GET").uri("/observations/getWhaleIdRange")
                .header("Accept", "text/html");
        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    /*
     * Test observation filter functions: create filter, get filtered list, remove
     * filter
     */
    @Test
    public void observationFilterTest() {

        // 1. create filter
        Http.RequestBuilder request1 = Helpers.fakeRequest()
                .method("POST")
                .bodyForm(ImmutableMap.of("date", "2020-12-01"))
                .uri("/observations/filter");
        Result result = route(app, request1);
        assertEquals(SEE_OTHER, result.status());

        // 2. get filtered list
        Http.RequestBuilder request2 = new Http.RequestBuilder().method("GET").uri("/observations/filter");
        result = route(app, request2);
        assertEquals(OK, result.status());
        if (result.contentType().isPresent() && result.charset().isPresent()) {
            assertEquals("text/html", result.contentType().get());
            assertEquals("utf-8", result.charset().get());
        } else {
            fail("Content type and/or charset not present in result");
        }

        // 3. remove filter
        Http.RequestBuilder request3 = Helpers.fakeRequest()
                .method("POST")
                .uri("/observations/filter/removeFilter");
        result = route(app, request3);
        assertEquals(SEE_OTHER, result.status());
    }

    /* Test if we can successfully redirect to the stats.scala.html view */
    @Test
    public void statsViewTest() {
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
