package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DeleteBookingTest extends BaseTest {
    @Test
    public void deleteBookingTest() {

        Response responseDelete = createBooking();
        responseDelete.print();

        int bookingid = responseDelete.jsonPath().getInt("bookingid");

        // call JSONObject
        JSONObject body = new JSONObject();

        // Use the delete method
        Response responseDeleteBooking = RestAssured.given(spec).auth().preemptive().basic("admin", "password123").contentType(ContentType.JSON).
                body(body.toString()).delete("/booking/" + bookingid);
        responseDeleteBooking.print();

        //Verify Response is 200
        Assert.assertEquals(responseDeleteBooking.getStatusCode(), 201, "Status Code should give a 201");

        Response responseGet = RestAssured.given(spec).get("/booking/" + bookingid);
        responseGet.print();

        Assert.assertEquals(responseGet.getBody().asString(), "Not Found");
    }
}
