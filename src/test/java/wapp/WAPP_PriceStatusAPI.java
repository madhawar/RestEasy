package wapp;

import org.testng.annotations.Test;
import specs.CommonGet;

import static io.restassured.RestAssured.given;

public class WAPP_PriceStatusAPI {
    private static final String url = "/price-status";

    CommonGet commonGet = new CommonGet();

    @Test()
    public void status200AndContentJSON() {
        String qpDomain = "WAPP";
        String qpExistingDate = "2021-06-11";
        String qpNewDate = "2021-06-11";
        String parameter = "result";

        commonGet.createRequestSpecification_3(qpDomain, qpExistingDate, qpNewDate);
        commonGet.createResponseSpecification_1(parameter);

        given()
                .spec(commonGet.requestSpec).log().uri()
                .when()
                .get(url)
                .then()
                .spec(commonGet.responseSpec).log().body();
    }
}
