package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PartialUpdateBookingTest extends BaseTest {
    @Test
    public void partialUpdateBookingTest() {

        // Create JSON Body
        Response responseCreate = createBooking();
        responseCreate.print();
        // Get bookingid of new booking
        int bookingid = responseCreate.jsonPath().getInt("bookingid");


        // Update firstname and checkin
        JSONObject body = new JSONObject();
        body.put("firstname", "Brock");

        JSONObject bookingdates = new JSONObject();
        bookingdates.put("checkin", "2020-03-24");
        bookingdates.put("checkout", "2020-03-26");
        body.put("bookingdates", bookingdates);


        // Use the patch method
        Response responsePartialUpdate = RestAssured.given(spec).auth().preemptive().basic("admin", "password123").contentType(ContentType.JSON).
                body(body.toString()).patch("/booking/" + bookingid);
        responsePartialUpdate.print();

        //Verify Response is 200
        Assert.assertEquals(responsePartialUpdate.getStatusCode(), 200, "Status Code should be 200 but it is not");

        // Verifications
        SoftAssert softAssert = new SoftAssert();
        String actualFirstName = responsePartialUpdate.jsonPath().getString("firstname");
        softAssert.assertEquals(actualFirstName, "Brock", "Name not recognized");

        String actualLastName = responsePartialUpdate.jsonPath().getString("lastname");
        softAssert.assertEquals(actualLastName, "Larsen", "Name not recognized");

        int price = responsePartialUpdate.jsonPath().getInt("totalprice");
        softAssert.assertEquals(price, 150, "price is showing incorrect");

        Boolean depositpaid = responsePartialUpdate.jsonPath().getBoolean("depositpaid");
        softAssert.assertFalse(depositpaid, "Deposit has been paid");

        String actualCheckin = responsePartialUpdate.jsonPath().getString("bookingdates.checkin");
        softAssert.assertEquals(actualCheckin, "2020-03-24", "Check-in date not recognized");

        String actualCheckout = responsePartialUpdate.jsonPath().getString("bookingdates.checkout");
        softAssert.assertEquals(actualCheckout, "2020-03-26", "Check-out date not recognized");

        softAssert.assertAll();
    }
}
