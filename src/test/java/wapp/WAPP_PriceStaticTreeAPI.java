package wapp;

import org.testng.annotations.Test;
import specs.CommonGet;

import static io.restassured.RestAssured.given;

public class WAPP_PriceStaticTreeAPI {
    private static final String url = "/price/static-tree";

    CommonGet commonGet = new CommonGet();

    @Test()
    public void status200AndContentJSON() {
        String qpDomain = "WAPP";
        String qpRequestDate = "2021-04-16";
        String parameter = "DTD";

        commonGet.createRequestSpecification_2(qpDomain, qpRequestDate);
        commonGet.createResponseSpecification_1(parameter);

        given()
                .spec(commonGet.requestSpec).log().uri()
                .when()
                .get(url)
                .then()
                .spec(commonGet.responseSpec).log().body();
    }
}
