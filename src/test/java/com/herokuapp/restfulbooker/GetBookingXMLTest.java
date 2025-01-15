package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GetBookingXMLTest extends BaseTest {
    @Test
    public void getBookingXMLTest() {
        // Create booking
        Response responseCreate = createBooking();
        responseCreate.print();

        // Set path parameter
        spec.pathParams("bookingid", responseCreate.jsonPath().getInt("bookingid"));

        // Get booking response with booking ids
        Header xml = new Header("Accept", "application/xml");
        spec.header(xml);
        Response response = RestAssured.given(spec).get("/booking/{bookingid}");
        response.print();

        //Verify Response is 200
        Assert.assertEquals(response.getStatusCode(), 200, "Status Code should be 200 but it is not");

        //Verify the firstname in the List is Sally
        SoftAssert softAssert = new SoftAssert();
        String actualFirstName = response.xmlPath().getString("booking.firstname");
        softAssert.assertEquals(actualFirstName, "Riley", "Name not recognized");

        String actualLastName = response.xmlPath().getString("booking.lastname");
        softAssert.assertEquals(actualLastName, "Larsen", "Name not recognized");

        int price = response.xmlPath().getInt("booking.totalprice");
        softAssert.assertEquals(price, 150, "price is showing incorrect");

        Boolean depositpaid = response.xmlPath().getBoolean("booking.depositpaid");
        softAssert.assertFalse(depositpaid, "Deposit has been paid");

        String actualCheckin = response.xmlPath().getString("booking.bookingdates.checkin");
        softAssert.assertEquals(actualCheckin, "2020-03-25", "Check-in date not recognized");

        String actualCheckout = response.xmlPath().getString("booking.bookingdates.checkout");
        softAssert.assertEquals(actualCheckout, "2020-03-27", "Check-out date not recognized");

        softAssert.assertAll();
    }
}
