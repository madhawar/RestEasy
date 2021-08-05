package wapp;

import org.testng.annotations.Test;
import specs.CommonGet;

import static io.restassured.RestAssured.given;

public class WAPP_PriceChangeLogsAPI {
    private static final String url = "/price-change-logs";

    CommonGet commonGet = new CommonGet();

    @Test()
    public void status200AndContentJSON() {
        String qpDomain = "WAPP";
        String parameter = "result";

        commonGet.createRequestSpecification_1(qpDomain);
        commonGet.createResponseSpecification_1(parameter);

        given()
                .spec(commonGet.requestSpec).log().uri()
                .when()
                .get(url)
                .then()
                .spec(commonGet.responseSpec).log().body();
    }
}
