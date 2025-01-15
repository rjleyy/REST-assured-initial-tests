package com.herokuapp.restfulbooker;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class CreateBookingTest extends BaseTest {
    @Test
    public void createBookingTest() {
        // Create booking
        Response response = createBooking();
        response.print();

        //Verify Response is 200
        Assert.assertEquals(response.getStatusCode(), 200, "Status Code should be 200 but it is not");

        // Verifications
        SoftAssert softAssert = new SoftAssert();
        String actualFirstName = response.jsonPath().getString("booking.firstname");
        softAssert.assertEquals(actualFirstName, "Riley", "Name not recognized");

        String actualLastName = response.jsonPath().getString("booking.lastname");
        softAssert.assertEquals(actualLastName, "Larsen", "Name not recognized");

        int price = response.jsonPath().getInt("booking.totalprice");
        softAssert.assertEquals(price, 150, "price is showing incorrect");

        Boolean depositpaid = response.jsonPath().getBoolean("booking.depositpaid");
        softAssert.assertFalse(depositpaid, "Deposit has been paid");

        String actualCheckin = response.jsonPath().getString("booking.bookingdates.checkin");
        softAssert.assertEquals(actualCheckin, "2020-03-25", "Check-in date not recognized");

        String actualCheckout = response.jsonPath().getString("booking.bookingdates.checkout");
        softAssert.assertEquals(actualCheckout, "2020-03-27", "Check-out date not recognized");

        softAssert.assertAll();

    }

    @Test
    public void createBookingTestWithPOJOTest() {
        // Create body using POJOs
        Bookingdates bookingdates = new Bookingdates("2020-03-24", "2020-03-26");
        Booking booking = new Booking("Riley", "Larsen", 150, false, bookingdates);


        // Get response
        Response response = RestAssured.given(spec).contentType(ContentType.JSON).body(booking)
                .post("/booking");
        response.print();

        // Create instance of Bookingid
        Bookingid bookingid = response.as(Bookingid.class);

        //Verify Response is 200
        Assert.assertEquals(response.getStatusCode(), 200, "Status Code should be 200 but it is not");

        System.out.println("Request booking: " + booking.toString());
        System.out.println("Response booking: " + bookingid.getBooking().toString());

        // Verifications
        Assert.assertEquals(bookingid.getBooking().toString(),booking.toString());


    }

}
