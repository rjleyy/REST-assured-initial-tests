package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GetBookingTest extends BaseTest {
    @Test
    public void getBookingTest() {
        // Create booking
        Response responseCreate = createBooking();
        responseCreate.print();

        // Set path parameter
        spec.pathParams("bookingId", responseCreate.jsonPath().getInt("bookingId"));

        // Get booking response with booking ids
        Response response = RestAssured.given(spec).get("/booking/{bookingId}");
        response.print();

        //Verify Response is 200
        Assert.assertEquals(response.getStatusCode(), 200, "Status Code should be 200 but it is not");

        //Verify the firstname in the List is Sally
        SoftAssert softAssert = new SoftAssert();
        String actualFirstName = response.jsonPath().getString("firstname");
        softAssert.assertEquals(actualFirstName, "Riley", "Name not recognized");

        String actualLastName = response.jsonPath().getString("lastname");
        softAssert.assertEquals(actualLastName, "Larsen", "Name not recognized");

        int price = response.jsonPath().getInt("totalprice");
        softAssert.assertEquals(price, 150, "price is showing incorrect");

        Boolean depositpaid = response.jsonPath().getBoolean("depositpaid");
        softAssert.assertFalse(depositpaid, "Deposit has been paid");

        String actualCheckin = response.jsonPath().getString("bookingdates.checkin");
        softAssert.assertEquals(actualCheckin, "2020-03-25", "Check-in date not recognized");

        String actualCheckout = response.jsonPath().getString("bookingdates.checkout");
        softAssert.assertEquals(actualCheckout, "2020-03-27", "Check-out date not recognized");
        /*
        {"firstname":"Eric",
        "lastname":"Smith",
        "totalprice":165,
        "depositpaid":false,
        "bookingdates":{"checkin":"2021-05-25",
        "checkout":"2023-01-09"}}
         */

        softAssert.assertAll();
    }
}
