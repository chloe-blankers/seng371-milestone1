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
    public void GoodCallTest(){
        //ParentController controller = app.injector().instanceOf(ParentController.class);
        RequestBuilder request = new RequestBuilder().method("GET").uri("/Whales/getWhales").header("Accept","application/txt+json");
        Result result = route(app,request);
        assertEquals(OK,result.status());
        assertNotEquals(BAD_REQUEST,result.status());
    }

    @Test
    public void BadMIMETest() {
        RequestBuilder request = new RequestBuilder().method("GET").uri("/Whales/getWhales").header("Accept","application/xml");
        Result result = route(app,request);
        assertEquals(BAD_REQUEST,result.status());
        assertNotEquals(OK,result.status());
    }

    @Test
    public void JsonBodyTest(){
        RequestBuilder request = new RequestBuilder().method("GET").uri("/Whales/getWhales").header("Accept","application/txt+json");
        Result result = route(app,request);
        if (result.contentType().isPresent())
            assertEquals("application/json",result.contentType().get());
        else
            fail("No value present in content type");
    }



}
