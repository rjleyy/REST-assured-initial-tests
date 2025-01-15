package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class UpdateBookingTest extends BaseTest {
    @Test
    public void updateBookingTest() {
        // Create JSON Body
        Response responseCreate = createBooking();
        responseCreate.print();
        // Get bookingid of new booking
        int bookingid = responseCreate.jsonPath().getInt("bookingid");


        // Update booking
        JSONObject body = new JSONObject();
        body.put("firstname", "Rachel");
        body.put("lastname", "Miner");
        body.put("totalprice", 125);
        body.put("depositpaid", false);

        JSONObject bookingdates = new JSONObject();
        bookingdates.put("checkin", "2020-03-25");
        bookingdates.put("checkout", "2020-03-27");
        body.put("bookingdates", bookingdates);

        // Update booking
        Response responseUpdate = RestAssured.given(spec).auth().preemptive().basic("admin", "password123").contentType(ContentType.JSON).
                body(body.toString()).put("/booking/" + bookingid);
        responseUpdate.print();

        //Verify Response is 200
        Assert.assertEquals(responseUpdate.getStatusCode(), 200, "Status Code should be 200 but it is not");

        // Verifications
        SoftAssert softAssert = new SoftAssert();
        String actualFirstName = responseUpdate.jsonPath().getString("firstname");
        softAssert.assertEquals(actualFirstName, "Rachel", "Name not recognized");

        String actualLastName = responseUpdate.jsonPath().getString("lastname");
        softAssert.assertEquals(actualLastName, "Miner", "Name not recognized");

        int price = responseUpdate.jsonPath().getInt("totalprice");
        softAssert.assertEquals(price, 125, "price is showing incorrect");

        Boolean depositpaid = responseUpdate.jsonPath().getBoolean("depositpaid");
        softAssert.assertFalse(depositpaid, "Deposit has been paid");

        String actualCheckin = responseUpdate.jsonPath().getString("bookingdates.checkin");
        softAssert.assertEquals(actualCheckin, "2020-03-25", "Check-in date not recognized");

        String actualCheckout = responseUpdate.jsonPath().getString("bookingdates.checkout");
        softAssert.assertEquals(actualCheckout, "2020-03-27", "Check-out date not recognized");

        softAssert.assertAll();

    }
}