package tests;

import org.testng.annotations.Test;
import specs.PostPriceMatrixPartial;
import utilities.Init;

import static io.restassured.RestAssured.given;

public class TestPartialPriceMatrix extends Init {
    private static final String url = "/price/wapp";

    PostPriceMatrixPartial postPriceMatrixPartial = new PostPriceMatrixPartial();

    @Test()
    public void singleTraveller() {
        postPriceMatrixPartial.createRequestSpecification();
        postPriceMatrixPartial.createResponseSpecification();

        given()
                .spec(postPriceMatrixPartial.requestSpec)
                .when()
                .post(url)
                .then()
                .spec(postPriceMatrixPartial.responseSpec)
                .log().all();
//                .extract()
//                .path("result.startDate" );

//        Assert.assertEquals(startDate, "2020-07-20");
    }

}
